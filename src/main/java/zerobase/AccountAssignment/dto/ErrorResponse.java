package zerobase.AccountAssignment.dto;


import lombok.*;
import zerobase.AccountAssignment.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String  errorMessage;
}
