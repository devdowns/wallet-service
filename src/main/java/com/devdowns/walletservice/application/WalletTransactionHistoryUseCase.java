package com.devdowns.walletservice.application;

import static java.util.Objects.isNull;

import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.WalletTransactionHistory;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.inputport.WalletTransactionInputPort;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class WalletTransactionHistoryUseCase implements WalletTransactionInputPort {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository walletTransactionRepository;

  public WalletTransactionHistoryUseCase(WalletRepository walletRepository,
      WalletTransactionRepository walletTransactionRepository) {
    this.walletRepository = walletRepository;
    this.walletTransactionRepository = walletTransactionRepository;
  }

  @Override
  public List<WalletTransactionHistory> getTransactionHistory(
      TransactionRequestFilter requestFilter) {

    if (isNull(requestFilter.getUserId())) {
      throw new MalformedRequestException("user_id must not be null");
    }

    final Wallet wallet = walletRepository.findByUserId(requestFilter.getUserId())
        .orElseThrow(() -> new WalletNotFoundException(requestFilter.getUserId()));

    final Pageable pageable = PageRequest.of(
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

    if (!isNull(requestFilter.getMinAmount())) {
      final BigDecimal minAmount = requestFilter.getMinAmount();
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(
          root.get("amount"),
          minAmount)
      );
    }

    if (!isNull(requestFilter.getMaxAmount())) {
      final BigDecimal maxAmount = requestFilter.getMaxAmount();
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(
          root.get("amount"),
          maxAmount)
      );
    }

    if (!isNull(requestFilter.getStartDate())) {
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(
          root.get("createdAt"),
          requestFilter.getStartDate().atStartOfDay())
      );
    }

    if (!isNull(requestFilter.getEndDate())) {
      spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(
          root.get("createdAt"),
          requestFilter.getEndDate().atTime(LocalTime.MAX))
      );
    }

    final List<WalletTransaction> transactions = walletTransactionRepository.findAll(spec, pageable)
        .toList();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    // Entity to DTO transformer
    return transactions.stream()
        .map(transaction -> WalletTransactionHistory.builder()
            .id(transaction.getId())
            .status(transaction.getTransactionStatus())
            .amount(transaction.getTransactionType().equals(TransactionType.WITHDRAW) ?
                transaction.getAmount().negate() : transaction.getAmount())
            .transactionType(transaction.getTransactionType())
            .date(transaction.getCreatedAt().format(formatter))
            .build())
        .collect(Collectors.toList());
  }
}
