package com.devdowns.walletservice.application;

import com.devdowns.walletservice.domain.entity.BankAccount;
import com.devdowns.walletservice.domain.entity.PaymentTransaction;
import com.devdowns.walletservice.domain.enums.PaymentStatus;
import com.devdowns.walletservice.infrastructure.inputport.PaymentInputPort;
import com.devdowns.walletservice.infrastructure.outputport.BankAccountRepository;
import com.devdowns.walletservice.infrastructure.outputport.PaymentTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class PaymentUseCase implements PaymentInputPort {

  private final BankAccountRepository bankAccountRepository;
  private final PaymentTransactionRepository paymentTransactionRepository;
  private final Random random = new Random();

  public PaymentUseCase(BankAccountRepository bankAccountRepository,
      PaymentTransactionRepository paymentTransactionRepository) {
    this.bankAccountRepository = bankAccountRepository;
    this.paymentTransactionRepository = paymentTransactionRepository;
  }

  public PaymentStatus processPayment(BankAccount source, BankAccount destination,
      BigDecimal amount) {
    BigDecimal netAmount = amount.abs();

    //  Transfer the funds
    source.setBalance(source.getBalance().subtract(netAmount));
    destination.setBalance(destination.getBalance().add(netAmount));
    bankAccountRepository.saveAll(List.of(source, destination));

    //  Check transaction status
    PaymentStatus status = generateRandomPaymentTransactionStatus();

    //  Check if a refund must be issued and grant it
    if (status.equals(PaymentStatus.FAILED)) {
      destination.setBalance(destination.getBalance().subtract(netAmount));
      source.setBalance(source.getBalance().add(netAmount));
      bankAccountRepository.saveAll(List.of(source, destination));

      //  Persist refund transaction
      createBankTransaction(destination, source, netAmount, PaymentStatus.COMPLETED);
    }

    createBankTransaction(source, destination, netAmount, status);
    return status;
  }

  private PaymentStatus generateRandomPaymentTransactionStatus() {
    return PaymentStatus.values()[random.nextInt(PaymentStatus.values().length)];
  }

  private void createBankTransaction(BankAccount source, BankAccount destination, BigDecimal amount,
      PaymentStatus status) {
    PaymentTransaction paymentTransaction = PaymentTransaction.builder()
        .paymentStatus(status)
        .transactionDate(LocalDateTime.now())
        .source(source)
        .amount(amount)
        .destination(destination)
        .build();
    paymentTransactionRepository.save(paymentTransaction);
  }
}

