package com.devdowns.walletservice.application;

import com.devdowns.walletservice.domain.entity.BankAccount;
import com.devdowns.walletservice.domain.entity.PaymentTransaction;
import com.devdowns.walletservice.domain.enums.PaymentStatus;
import com.devdowns.walletservice.infrastructure.inputport.PaymentInputPort;
import com.devdowns.walletservice.infrastructure.outputport.BankAccountRepository;
import com.devdowns.walletservice.infrastructure.outputport.PaymentTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    final BigDecimal netAmount = amount.abs();

    //  Pre-emptively remove funds from the source account
    source.setBalance(source.getBalance().subtract(netAmount));
    bankAccountRepository.save(source);

    //  Check if the transaction goes through or not
    final PaymentStatus status = generateRandomPaymentTransactionStatus();

    //  Check if a refund must be issued and grant it
    if (status.equals(PaymentStatus.FAILED)) {
      createBankTransaction(destination, source, netAmount, status);
      updateAccountBalance(source, netAmount);
    } else if (status.equals(PaymentStatus.COMPLETED)) {
      createBankTransaction(source, destination, netAmount, status);
      updateAccountBalance(destination, netAmount);
    }
    return status;
  }

  private PaymentStatus generateRandomPaymentTransactionStatus() {
    return PaymentStatus.values()[random.nextInt(PaymentStatus.values().length)];
  }

  private void updateAccountBalance(BankAccount bankAccount, BigDecimal amount) {
    bankAccount.setBalance(bankAccount.getBalance().add(amount));
    bankAccountRepository.save(bankAccount);
  }

  private void createBankTransaction(BankAccount source, BankAccount destination, BigDecimal amount,
      PaymentStatus status) {
    paymentTransactionRepository.save(
        PaymentTransaction.builder()
            .paymentStatus(status)
            .transactionDate(LocalDateTime.now())
            .source(source)
            .amount(amount)
            .destination(destination)
            .build()
    );
  }
}

