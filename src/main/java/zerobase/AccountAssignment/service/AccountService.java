package zerobase.AccountAssignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.AccountAssignment.domain.Account;
import zerobase.AccountAssignment.domain.AccountUser;
import zerobase.AccountAssignment.dto.AccountDto;
import zerobase.AccountAssignment.exception.AccountException;
import zerobase.AccountAssignment.repository.AccountRepository;
import zerobase.AccountAssignment.repository.AccountUserRepository;
import zerobase.AccountAssignment.type.AccountStatus;

import java.util.List;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static zerobase.AccountAssignment.type.AccountStatus.IN_USE;
import static zerobase.AccountAssignment.type.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    //사용자가 있는지 조회
    //계좌의 번호를 생성하고 계좌를 저장하고, 그 정보를 넘긴다
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {
        AccountUser accountUser = getAccountUser(userId);

        validateCreateAccount(accountUser);

        //최근에 생성된 계좌를 가져옴
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber()))+1+"")
                .orElse("1000000000");

        return AccountDto.fromEntity(
                //신규계좌 저장
                accountRepository.save(
                        Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build()
                ));
    }

    private void validateCreateAccount(AccountUser accountUser) {
        if(accountRepository.countByAccountUser(accountUser) >= 10){
            throw new AccountException(MAX_ACCOUNT_PER_USER_10);
        }
    }

    @Transactional
    public Account getAccount(Long id) {
        if(id < 0){
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }

    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = getAccountUser(userId);

        Account account = accountRepository.findByAccountNumber(accountNumber).
                orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        
        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(AccountStatus.UNREGISTERED);
        account.setUnRegisteredAt(LocalDateTime.now());

        accountRepository.save(account);

        return AccountDto.fromEntity(account);
    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) {
        if(accountUser.getId() != account.getAccountUser().getId()) {
            throw new AccountException(USER_ACCOUNT_UN_MATCH);
        }
        if(account.getAccountStatus() == AccountStatus.UNREGISTERED){
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }
        if(account.getBalance() > 0){
            throw new AccountException(BALANCE_NOT_EMPTY);
        }
    }

    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = getAccountUser(userId);

        List<Account> accounts = accountRepository.findByAccountUser(accountUser);

        return accounts.stream().map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    private AccountUser getAccountUser(Long userId) {
        AccountUser accountUser = accountUserRepository.
                findById(userId).orElseThrow(()
                        -> new AccountException(USER_NOT_FOUND));
        return accountUser;
    }
}
