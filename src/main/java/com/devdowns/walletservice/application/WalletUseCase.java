package com.devdowns.walletservice.application;

import static java.util.Objects.isNull;

import com.devdowns.walletservice.domain.dto.wallet.BalanceResponse;
import com.devdowns.walletservice.domain.dto.wallet.CreateWalletTransactionRequest;
import com.devdowns.walletservice.domain.dto.wallet.TransactionCreatedResponse;
import com.devdowns.walletservice.domain.entity.BankAccount;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.enums.PaymentStatus;
import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.devdowns.walletservice.domain.exception.InsufficientBalanceException;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.UserNotFoundException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.inputport.PaymentInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import com.devdowns.walletservice.infrastructure.outputport.BankRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import jakarta.ws.rs.InternalServerErrorException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class WalletUseCase implements WalletInputPort {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository walletTransactionRepository;
  private final PaymentInputPort paymentProcessor;
  private final BankRepository bankRepository;

  public WalletUseCase(WalletRepository walletRepository,
      WalletTransactionRepository walletTransactionRepository, PaymentUseCase paymentProcessor,
      BankRepository bankRepository) {
    this.walletRepository = walletRepository;
    this.walletTransactionRepository = walletTransactionRepository;
    this.paymentProcessor = paymentProcessor;
    this.bankRepository = bankRepository;
  }

  @Override
  public BalanceResponse checkBalance(Long userId) {

    if (userId == null) {
      throw new MalformedRequestException("user_id must not be null");
    }

    Wallet wallet = walletRepository.findByUserId(userId)
        .orElseThrow(() -> new WalletNotFoundException(userId));

    return BalanceResponse.builder()
        .userId(userId)
        .balance(wallet.getBalance())
        .build();
  }

  @Override
  public TransactionCreatedResponse createTransaction(CreateWalletTransactionRequest request) {

    validateTransactionRequest(request);

    handleSpecialUserIds(request.getUserId());

    Wallet wallet = findWalletByUserId(request.getUserId());

    validateSufficientBalance(wallet, request.getAmount());

    WalletTransaction walletTransaction = createWalletTransaction(request, wallet);

    updateWalletBalance(wallet, request.getAmount());

    //  Bank set in cache
    BankAccount thirdPartyBank = bankRepository.findById(2L).get();

    TransactionType transactionType = walletTransaction.getTransactionType();

    // Determine which are the source and destination accounts
    // based on transaction type
    PaymentStatus status = transactionType.equals(TransactionType.WITHDRAW) ?
        paymentProcessor.processPayment(wallet.getBankAccount(), thirdPartyBank,
            request.getAmount())
        : paymentProcessor.processPayment(thirdPartyBank, wallet.getBankAccount(),
            request.getAmount());

    //  Handle PaymentStatus responses from the payment processor
    if (status.equals(PaymentStatus.FAILED) && transactionType.equals(TransactionType.WITHDRAW)) {
      //  Update wallet transaction status with the payment status
      updateTransactionStatus(walletTransaction, TransactionStatus.FAILED);
      updateWalletBalance(wallet, request.getAmount().abs());
      createRefundWalletTransaction(wallet, request.getAmount().abs());
    } else if (status.equals(PaymentStatus.FAILED) && transactionType.equals(
        TransactionType.DEPOSIT)) {
      updateTransactionStatus(walletTransaction, TransactionStatus.FAILED);
    } else {
      updateTransactionStatus(walletTransaction, TransactionStatus.COMPLETED);
    }

    return buildTransactionCreatedResponse(walletTransaction, request);
  }

  private void updateTransactionStatus(WalletTransaction walletTransaction,
      TransactionStatus status) {
    walletTransaction.setTransactionStatus(status);
    walletTransactionRepository.save(walletTransaction);
  }

  private void validateTransactionRequest(CreateWalletTransactionRequest request) {
    if (isNull(request.getAmount()) || isNull(request.getUserId())) {
      throw new MalformedRequestException("amount and user_id must not be null");
    }
  }

  private void handleSpecialUserIds(Long userId) {
    switch (userId.intValue()) {
      case 404 -> throw new UserNotFoundException();
      case 500 -> throw new InternalServerErrorException("Something went wrong on the server");
      default -> {
      }
    }
  }

  private Wallet findWalletByUserId(Long userId) {
    return walletRepository.findByUserId(userId)
        .orElseThrow(() -> new WalletNotFoundException(userId));
  }

  private void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
    if (amount.negate().compareTo(wallet.getBalance()) > 0) {
      throw new InsufficientBalanceException();
    }
  }

  private WalletTransaction createWalletTransaction(CreateWalletTransactionRequest request,
      Wallet wallet) {

    TransactionType transactionType =
        request.getAmount().compareTo(BigDecimal.ZERO) < 0 ? TransactionType.WITHDRAW
            : TransactionType.DEPOSIT;

    WalletTransaction walletTransaction = WalletTransaction.builder()
        .wallet(wallet)
        .amount(request.getAmount().abs())
        .transactionStatus(TransactionStatus.PROCESSING)
        .transactionType(transactionType)
        .build();

    return walletTransactionRepository.save(walletTransaction);
  }

  private void createRefundWalletTransaction(Wallet wallet, BigDecimal amount) {
    WalletTransaction refundedTransaction = WalletTransaction.builder()
        .wallet(wallet)
        .amount(amount)
        .transactionStatus(TransactionStatus.REFUNDED)
        .transactionType(TransactionType.DEPOSIT)
        .build();
    walletTransactionRepository.save(refundedTransaction);
  }

  private void updateWalletBalance(Wallet wallet, BigDecimal amount) {
    wallet.setBalance(wallet.getBalance().add(amount));
    walletRepository.save(wallet);
  }

  private TransactionCreatedResponse buildTransactionCreatedResponse(WalletTransaction transaction,
      CreateWalletTransactionRequest request) {
    return TransactionCreatedResponse.builder()
        .walletTransactionId(transaction.getId())
        .userId(request.getUserId())
        .amount(request.getAmount())
        .build();
  }
}
