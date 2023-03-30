package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.BalanceResponse;
import com.devdowns.walletservice.domain.dto.CreateTransactionRequest;
import com.devdowns.walletservice.domain.dto.TransactionCreatedResponse;
import jakarta.transaction.Transactional;

public interface WalletInputPort {
    @Transactional
    TransactionCreatedResponse createTransaction(CreateTransactionRequest request);

    BalanceResponse checkBalance(Long userId);
};
