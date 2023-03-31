package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.WalletTransactionHistory;
import java.util.List;

public interface WalletTransactionInputPort {

  List<WalletTransactionHistory> getTransactionHistory(TransactionRequestFilter requestFilter);

}
