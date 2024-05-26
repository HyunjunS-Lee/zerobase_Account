package zerobase.AccountAssignment.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.AccountAssignment.domain.Account;
import zerobase.AccountAssignment.domain.AccountUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByIdDesc();    //역순으로 값을 가져옴

    Integer countByAccountUser(AccountUser accountUser);

    Optional<Account> findByAccountNumber(String AccountNumber);

    List<Account> findByAccountUser(AccountUser accountUser);
}
