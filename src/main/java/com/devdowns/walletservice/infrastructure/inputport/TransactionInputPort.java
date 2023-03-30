package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.TransactionHistory;
import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;

import java.util.List;

public interface TransactionInputPort {
    List<TransactionHistory> getTransactionHistory(TransactionRequestFilter requestFilter);

}
