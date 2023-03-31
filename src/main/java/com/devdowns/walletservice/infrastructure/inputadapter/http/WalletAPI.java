package com.devdowns.walletservice.infrastructure.inputadapter.http;

import com.devdowns.walletservice.domain.dto.wallet.BalanceResponse;
import com.devdowns.walletservice.domain.dto.wallet.CreateWalletTransactionRequest;
import com.devdowns.walletservice.domain.dto.wallet.TransactionCreatedResponse;
import com.devdowns.walletservice.domain.dto.wallet.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.wallet.WalletTransactionHistory;
import com.devdowns.walletservice.infrastructure.inputport.TransactionInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
public class WalletAPI {

  private final WalletInputPort walletInputPort;
  private final TransactionInputPort transactionInputPort;

  public WalletAPI(WalletInputPort walletInputPort, TransactionInputPort transactionInputPort) {
    this.walletInputPort = walletInputPort;
    this.transactionInputPort = transactionInputPort;
  }

  @PostMapping("/transactions")
  public ResponseEntity<TransactionCreatedResponse> createWalletTransaction(
      @RequestBody CreateWalletTransactionRequest request) {
    TransactionCreatedResponse response = walletInputPort.createTransaction(request);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/balance")
  public ResponseEntity<BalanceResponse> getBalance(@RequestParam("user_id") Long userId) {
    BalanceResponse response = walletInputPort.checkBalance(userId);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/transactions/history")
  public ResponseEntity<List<WalletTransactionHistory>> getHistory(
      @RequestBody TransactionRequestFilter requestFilter) {
    List<WalletTransactionHistory> response = transactionInputPort.getTransactionHistory(
        requestFilter);
    return ResponseEntity.ok().body(response);
  }
}
