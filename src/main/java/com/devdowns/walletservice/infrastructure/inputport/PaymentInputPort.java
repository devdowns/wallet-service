package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.entity.BankAccount;
import com.devdowns.walletservice.domain.enums.PaymentStatus;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

public interface PaymentInputPort {

  @Transactional
  public PaymentStatus processPayment(BankAccount source, BankAccount destination,
      BigDecimal amount);
}
