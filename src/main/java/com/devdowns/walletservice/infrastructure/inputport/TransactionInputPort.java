package com.devdowns.walletservice.infrastructure.inputport;

import com.devdowns.walletservice.domain.dto.wallet.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.wallet.WalletTransactionHistory;
import java.util.List;

public interface TransactionInputPort {

  List<WalletTransactionHistory> getTransactionHistory(TransactionRequestFilter requestFilter);

}
