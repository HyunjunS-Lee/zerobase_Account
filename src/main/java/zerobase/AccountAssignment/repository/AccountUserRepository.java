package zerobase.AccountAssignment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.AccountAssignment.domain.AccountUser;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
}
