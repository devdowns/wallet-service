package com.devdowns.walletservice.application;

import com.devdowns.walletservice.domain.dto.TransactionHistory;
import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.inputport.TransactionInputPort;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionHistoryUseCase implements TransactionInputPort {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository walletTransactionRepository;

  public TransactionHistoryUseCase(WalletRepository walletRepository,
      WalletTransactionRepository walletTransactionRepository) {
    this.walletRepository = walletRepository;
    this.walletTransactionRepository = walletTransactionRepository;
  }

  @Override
  public List<TransactionHistory> getTransactionHistory(TransactionRequestFilter requestFilter) {

    if (requestFilter.getUserId() == null) {
      throw new MalformedRequestException("user_id must not be null");
    }

    Wallet wallet = walletRepository.findByUserId(requestFilter.getUserId())
        .orElseThrow(() -> new WalletNotFoundException(requestFilter.getUserId()));

    Pageable pageable = PageRequest.of(
        requestFilter.getPage(),
        requestFilter.getSize(),
        Sort.by("createdAt").descending()
    );

    // Transaction History Filter Specification for date and amount filters
    Specification<WalletTransaction> spec = Specification.where(null);

    spec = spec.and((root, query, cb) -> cb.equal(
        root.get("wallet").get("id"),
        wallet.getId())
    );

    if (requestFilter.getMinAmount() != null) {
      BigDecimal minAmount = requestFilter.getMinAmount();
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(
          root.get("amount"),
          minAmount)
      );
    }

    if (requestFilter.getMaxAmount() != null) {
      BigDecimal maxAmount = requestFilter.getMaxAmount();
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(
          root.get("amount"),
          maxAmount)
      );
    }

    if (requestFilter.getStartDate() != null) {
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(
          root.get("createdAt"),
          requestFilter.getStartDate().atStartOfDay())
      );
    }

    if (requestFilter.getEndDate() != null) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(
          root.get("createdAt"),
          requestFilter.getEndDate().atTime(LocalTime.MAX))
      );
    }

    List<WalletTransaction> transactions = walletTransactionRepository.findAll(spec, pageable)
        .toList();

    // Entity to DTO transformer
    return transactions.stream()
        .map(transaction -> TransactionHistory.builder()
            .id(transaction.getId())
            .status(transaction.getTransactionStatus())
            .amount(transaction.getAmount())
            .transactionType(transaction.getTransactionType())
            .date(transaction.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }
}
