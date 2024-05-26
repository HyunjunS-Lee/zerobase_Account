package zerobase.AccountAssignment.domain;


import lombok.*;
import zerobase.AccountAssignment.type.TransactionResultType;
import zerobase.AccountAssignment.type.TransactionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne
    private Account account;
    private Long amount;
    private Long balanceSnapShot;

    private String transactionId;
    private LocalDateTime transactedAt;

}
