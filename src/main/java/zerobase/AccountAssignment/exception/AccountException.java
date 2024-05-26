package zerobase.AccountAssignment.exception;

import lombok.*;
import zerobase.AccountAssignment.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends  RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public AccountException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
