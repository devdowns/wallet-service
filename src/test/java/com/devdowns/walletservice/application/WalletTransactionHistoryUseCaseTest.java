package com.devdowns.walletservice.application;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.WalletTransactionHistory;
import com.devdowns.walletservice.domain.entity.User;
import com.devdowns.walletservice.domain.entity.Wallet;
import com.devdowns.walletservice.domain.entity.WalletTransaction;
import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import com.devdowns.walletservice.infrastructure.outputport.WalletRepository;
import com.devdowns.walletservice.infrastructure.outputport.WalletTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private TransactionRequestFilter permissiveFilter, restrictiveFilter;

  @BeforeEach
  public void setup() {
    permissiveFilter = TransactionRequestFilter.builder()
        .userId(1L)
        .page(0)
        .size(2)
        .startDate(LocalDate.now().minusDays(25))
        .endDate(LocalDate.now())
        .minAmount(BigDecimal.ONE)
        .maxAmount(BigDecimal.TEN)
        .build();

    restrictiveFilter = TransactionRequestFilter.builder()
        .userId(1L)
        .page(0)
        .startDate(LocalDate.now().plusYears(2))
        .size(1)
        .build();

    wallet = new Wallet();
    wallet.setId(1L);
    wallet.setUser(User.builder().id(1L).build());
    wallet.setBalance(new BigDecimal(100));

    final List<WalletTransaction> walletTransactionsList = List.of(
        new WalletTransaction(1L, wallet, new BigDecimal(50),
            LocalDateTime.now().minusDays(3), TransactionStatus.COMPLETED,
            TransactionType.DEPOSIT),
        new WalletTransaction(2L, wallet, new BigDecimal(17),
            LocalDateTime.now().minusDays(2), TransactionStatus.FAILED, TransactionType.WITHDRAW)
    );
  }

  @Test
  public void testTransactionHistoryIsEmptyListIfNoneMatchFilter() {

    // Arrange
    when(walletRepository.findByUserId(restrictiveFilter.getUserId())).thenReturn(
        Optional.of(wallet));
    when(walletTransactionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

    // Act
    final List<WalletTransactionHistory> walletTransactionHistoryList = transactionHistoryUseCase.getTransactionHistory(
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
