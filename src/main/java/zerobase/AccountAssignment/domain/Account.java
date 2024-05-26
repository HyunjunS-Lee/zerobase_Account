package zerobase.AccountAssignment.domain;

import lombok.*;
import zerobase.AccountAssignment.exception.AccountException;
import zerobase.AccountAssignment.type.AccountStatus;
import zerobase.AccountAssignment.type.ErrorCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends BaseEntity{

    @ManyToOne
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING)//INUSE, UNREGISTERED가 그대로 문자로 DB테이블에 저장
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public void useBalance(Long amount) {
        if(amount > balance) {
            throw new AccountException(ErrorCode.ACCOUNT_EXCEED_BALANCE);
        }
        balance -= amount;
    }

    public void cancelBalance(Long amount) {
        if(amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        balance += amount;
    }
}
