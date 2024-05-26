package zerobase.AccountAssignment.domain;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AccountUser extends BaseEntity{ //Entity 클래스
    private String name;
}
