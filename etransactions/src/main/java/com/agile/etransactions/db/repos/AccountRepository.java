package com.agile.etransactions.db.repos;

import com.agile.etransactions.db.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    boolean existsByIban(String iban);

    Account findAccountByIban(String iban);

}
