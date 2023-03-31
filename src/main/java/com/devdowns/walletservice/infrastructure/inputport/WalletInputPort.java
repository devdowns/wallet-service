package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.wallet.BalanceResponse;
import com.devdowns.walletservice.domain.dto.wallet.CreateWalletTransactionRequest;
import com.devdowns.walletservice.domain.dto.wallet.TransactionCreatedResponse;
import jakarta.transaction.Transactional;

public interface WalletInputPort {

  @Transactional
  TransactionCreatedResponse createTransaction(CreateWalletTransactionRequest request);

  BalanceResponse checkBalance(Long userId);
};
