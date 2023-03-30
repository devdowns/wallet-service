package com.devdowns.walletservice.application;

import static java.util.Objects.isNull;

import com.devdowns.walletservice.domain.dto.BalanceResponse;
import com.devdowns.walletservice.domain.dto.CreateTransactionRequest;
import com.devdowns.walletservice.domain.dto.TransactionCreatedResponse;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.devdowns.walletservice.domain.exception.InsufficientBalanceException;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.UserNotFoundException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import jakarta.ws.rs.InternalServerErrorException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class WalletUseCase implements WalletInputPort {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository walletTransactionRepository;

  public WalletUseCase(WalletRepository walletRepository,
      WalletTransactionRepository walletTransactionRepository) {
    this.walletRepository = walletRepository;
    this.walletTransactionRepository = walletTransactionRepository;
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
  public TransactionCreatedResponse createTransaction(CreateTransactionRequest request) {

    validateCreateTransactionRequest(request);

    handleSpecialUserIds(request.getUserId());

    Wallet wallet = findWalletByUserId(request.getUserId());

    validateSufficientBalance(wallet, request.getAmount());

    WalletTransaction transactionRequest = createWalletTransaction(request, wallet);

    walletTransactionRepository.save(transactionRequest);

    updateWalletBalance(wallet, request.getAmount());

    return buildTransactionCreatedResponse(transactionRequest, request);
  }

  private void validateCreateTransactionRequest(CreateTransactionRequest request) {
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

  private WalletTransaction createWalletTransaction(CreateTransactionRequest request,
      Wallet wallet) {

    TransactionType transactionType =
        request.getAmount().compareTo(BigDecimal.ZERO) < 0 ? TransactionType.WITHDRAW
            : TransactionType.DEPOSIT;

    return WalletTransaction.builder()
        .wallet(wallet)
        .amount(request.getAmount().abs())
        .transactionStatus(TransactionStatus.PROCESSING)
        .transactionType(transactionType)
        .build();
  }

  private void updateWalletBalance(Wallet wallet, BigDecimal amount) {
    wallet.setBalance(wallet.getBalance().add(amount));
    walletRepository.save(wallet);
  }

  private TransactionCreatedResponse buildTransactionCreatedResponse(WalletTransaction transaction,
      CreateTransactionRequest request) {
    return TransactionCreatedResponse.builder()
        .walletTransactionId(transaction.getId())
        .userId(request.getUserId())
        .amount(request.getAmount())
        .build();
  }

}
