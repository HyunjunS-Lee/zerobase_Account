package zerobase.AccountAssignment.dto;


import lombok.*;
import zerobase.AccountAssignment.domain.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long useId;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .useId(account.getAccountUser().getId()).
                accountNumber(account.getAccountNumber()).
                balance(account.getBalance()).
                registeredAt(account.getRegisteredAt()).
                unRegisteredAt(account.getUnRegisteredAt()).
                build();
    }
}
