package zerobase.AccountAssignment.dto;


import lombok.*;
import zerobase.AccountAssignment.domain.Transaction;
import zerobase.AccountAssignment.type.TransactionResultType;
import zerobase.AccountAssignment.type.TransactionType;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResultType transactionResultType;
    private Long amount;
    private Long balanceSnapShot;
    private String transactionId;
    private LocalDateTime transactedAt;

    public static TransactionDto fromEntity(Transaction transaction) {
         return TransactionDto.builder().
        accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .amount(transaction.getAmount())
                .balanceSnapShot(transaction.getBalanceSnapShot())
                .transactionId(transaction.getTransactionId())
                .transactedAt(transaction.getTransactedAt())
                .build();

        // return TransactionDto.fromEntity(transaction);
    }
}
