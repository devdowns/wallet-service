package com.devdowns.walletservice.infrastructure.outputport;

import com.devdowns.walletservice.domain.entity.BankAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

  Optional<BankAccount> findByAccountNumberAndRoutingNumber(String accountNumber,
      String routingNumber);

  @Query("SELECT ba FROM BankAccount ba WHERE ba.bankName = 'OnTop'")
  Optional<BankAccount> getOnTopBankAccount();

}

