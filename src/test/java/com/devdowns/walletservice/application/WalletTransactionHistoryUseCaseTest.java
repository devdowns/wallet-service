package com.devdowns.walletservice.application;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.devdowns.walletservice.domain.dto.wallet.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.wallet.WalletTransactionHistory;
import com.devdowns.walletservice.domain.entity.User;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class WalletTransactionHistoryUseCaseTest {

  @Mock
  private WalletRepository walletRepository;

  @Mock
  private WalletTransactionRepository walletTransactionRepository;

  @InjectMocks
  private WalletTransactionHistoryUseCase transactionHistoryUseCase;

  private Wallet wallet;
  private WalletTransaction transaction, transaction2;
  private TransactionRequestFilter permissiveFilter, restrictiveFilter;

  @BeforeEach
  public void setup() {
    User user = User.builder()
        .id(1L)
        .build();

    wallet = Wallet.builder()
        .user(user)
        .id(1L)
        .build();

    transaction = WalletTransaction.builder()
        .id(1L)
        .amount(BigDecimal.TEN)
        .createdAt(LocalDate.now().atStartOfDay())
        .wallet(wallet)
        .transactionStatus(TransactionStatus.COMPLETED)
        .build();

    transaction2 = WalletTransaction.builder()
        .id(2L)
        .amount(BigDecimal.ONE)
        .createdAt(LocalDate.now().atTime(LocalTime.MAX))
        .wallet(wallet)
        .transactionStatus(TransactionStatus.COMPLETED)
        .build();

    permissiveFilter = TransactionRequestFilter.builder()
        .userId(1L)
        .page(0)
        .size(1)
        .startDate(LocalDate.now().minusDays(15))
        .endDate(LocalDate.now())
        .minAmount(BigDecimal.ONE)
        .maxAmount(BigDecimal.TEN)
        .build();

    permissiveFilter = TransactionRequestFilter.builder()
        .userId(1L)
        .page(0)
        .size(1)
        .startDate(LocalDate.now().minusDays(15))
        .endDate(LocalDate.now())
        .minAmount(BigDecimal.ONE)
        .maxAmount(BigDecimal.TEN)
        .build();

    restrictiveFilter = TransactionRequestFilter.builder()
        .minAmount(new BigDecimal(100000000))
        .page(1)
        .size(1)
        .userId(wallet.getUser().getId())
        .build();
  }

  @Test
  public void testTransactionHistoryInDescendingOrder() {

    // Arrange
    when(walletRepository.findByUserId(permissiveFilter.getUserId())).thenReturn(
        Optional.of(wallet));
    when(walletTransactionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(transaction, transaction2)));

    // Act
    List<WalletTransactionHistory> walletTransactionHistoryList = transactionHistoryUseCase.getTransactionHistory(
        permissiveFilter);
    WalletTransactionHistory walletTransactionHistory = walletTransactionHistoryList.get(1);

    // Assert
    verify(walletRepository).findByUserId(wallet.getUser().getId());
    verify(walletTransactionRepository).findAll(any(Specification.class), any(Pageable.class));

    Assertions.assertEquals(2, walletTransactionHistoryList.size());
    Assertions.assertTrue(transaction2.getCreatedAt().isAfter(transaction.getCreatedAt()));
    Assertions.assertEquals(transaction2.getId(), walletTransactionHistory.getId());
    Assertions.assertEquals(transaction2.getAmount(), walletTransactionHistory.getAmount());
    Assertions.assertEquals(transaction2.getTransactionStatus(),
        walletTransactionHistory.getStatus());
    Assertions.assertEquals(transaction2.getCreatedAt(), walletTransactionHistory.getDate());
  }

  @Test
  public void testTransactionHistoryIsEmptyListIfNoneMatchFilter() {

    // Arrange
    when(walletRepository.findByUserId(restrictiveFilter.getUserId())).thenReturn(
        Optional.of(wallet));
    when(walletTransactionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

    // Act
    List<WalletTransactionHistory> walletTransactionHistoryList = transactionHistoryUseCase.getTransactionHistory(
        restrictiveFilter);

    // Assert
    verify(walletRepository).findByUserId(wallet.getUser().getId());
    verify(walletTransactionRepository).findAll(any(Specification.class), any(Pageable.class));

    Assertions.assertTrue(walletTransactionHistoryList.isEmpty());
  }

  @Test
  public void testGetTransactionHistoryWithInvalidUserId() {

    // Arrange
    permissiveFilter.setUserId(null);

    // Act & Assert
    Assertions.assertThrows(MalformedRequestException.class, () -> {
      transactionHistoryUseCase.getTransactionHistory(permissiveFilter);
    });

    verifyNoInteractions(walletRepository);
    verifyNoInteractions(walletTransactionRepository);
  }

  @Test
  public void testGetTransactionHistoryWithNonExistingWallet() {

    // Arrange
    when(walletRepository.findByUserId(restrictiveFilter.getUserId())).thenReturn(Optional.empty());

    // Act & Assert
    Assertions.assertThrows(WalletNotFoundException.class, () -> {
      transactionHistoryUseCase.getTransactionHistory(restrictiveFilter);
    });

    verify(walletRepository).findByUserId(restrictiveFilter.getUserId());
    verifyNoInteractions(walletTransactionRepository);
  }

}
