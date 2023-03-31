package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.BalanceResponse;
import com.devdowns.walletservice.domain.dto.CreateWalletTransactionRequest;
import com.devdowns.walletservice.domain.dto.TransactionCreatedResponse;
import jakarta.transaction.Transactional;

public interface WalletInputPort {

  @Transactional
  TransactionCreatedResponse createTransaction(CreateWalletTransactionRequest request);

  BalanceResponse checkBalance(Long userId);
};
