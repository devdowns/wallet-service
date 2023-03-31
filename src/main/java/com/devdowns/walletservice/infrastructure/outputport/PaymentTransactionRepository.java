package com.devdowns.walletservice.infrastructure.outputport;

import com.devdowns.walletservice.domain.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
  
}
