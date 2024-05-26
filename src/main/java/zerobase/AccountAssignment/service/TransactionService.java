package zerobase.AccountAssignment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zerobase.AccountAssignment.domain.Account;
import zerobase.AccountAssignment.domain.AccountUser;
import zerobase.AccountAssignment.domain.Transaction;
import zerobase.AccountAssignment.dto.TransactionDto;
import zerobase.AccountAssignment.exception.AccountException;
import zerobase.AccountAssignment.repository.AccountRepository;
import zerobase.AccountAssignment.repository.AccountUserRepository;
import zerobase.AccountAssignment.repository.TransactionRepository;
import zerobase.AccountAssignment.type.AccountStatus;
import zerobase.AccountAssignment.type.ErrorCode;
import zerobase.AccountAssignment.type.TransactionResultType;
import zerobase.AccountAssignment.type.TransactionType;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static zerobase.AccountAssignment.type.TransactionResultType.F;
import static zerobase.AccountAssignment.type.TransactionResultType.S;
import static zerobase.AccountAssignment.type.TransactionType.CANCEL;
import static zerobase.AccountAssignment.type.TransactionType.USE;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionDto useBalance(Long userId, String accountNumber, Long amount) {
        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(()->new AccountException(ErrorCode.USER_NOT_FOUND));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(()->new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateUseBalance(user, account, amount);

        account.useBalance(amount);

        return TransactionDto.fromEntity(saveAndGetTransaction(USE, S, account, amount));
    }

    private void validateUseBalance(AccountUser user, Account account, Long amount) {
        if(!Objects.equals(user.getId(), account.getAccountUser().getId())){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        } //사용자 아이디
        if(account.getAccountStatus() != AccountStatus.IN_USE){
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        } //계좌 소유주
        if(account.getBalance() < amount) {
            throw new AccountException(ErrorCode.ACCOUNT_EXCEED_BALANCE);
        } //거래금액이 큰경우
    }

    @Transactional
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        saveAndGetTransaction(USE,F, account, amount);
    }

    private Transaction saveAndGetTransaction(
            TransactionType transactionType,
            TransactionResultType transactionResultType, Account account, Long amount) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionType(transactionType)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapShot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public TransactionDto cancelBalance(String transactionId, String accountNumber, Long amount) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(()->new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateCancelBalance(transaction, account, amount);

        account.cancelBalance(amount);

        return TransactionDto.fromEntity(saveAndGetTransaction(CANCEL, S, account, amount));
    }

    private void validateCancelBalance(Transaction transaction, Account account, Long amount) {
        if(!Objects.equals(transaction.getAccount().getId(), account.getId())){
            throw new AccountException(ErrorCode.TRANSACTION_ACCOUNT_UN_MATCH);
        }
        if(!Objects.equals(transaction.getAmount(), amount)){
            throw new AccountException(ErrorCode.CANCEL_MUST_FULLY);
        }
        if(transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))){
            throw new AccountException(ErrorCode.TOO_OLD_ORDER_TO_CANCEL);
        }
    }

    @Transactional
    public void saveFailedCancelTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        saveAndGetTransaction(CANCEL, F, account, amount);
    }

    public TransactionDto queryTransaction(String transactionId) {
        return TransactionDto.fromEntity(transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(()->new AccountException(ErrorCode.TRANSACTION_NOT_FOUND)));
    }
}
