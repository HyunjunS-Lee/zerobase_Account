package zerobase.AccountAssignment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//DB에 업데이트하거나 저장할 때 @CREATEDDATE같은 어노테이션이 붙은 값들을 자동으로 저장해줌
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
