package com.devdowns.walletservice.infrastructure.inputadapter.http;

import com.devdowns.walletservice.domain.dto.BalanceResponse;
import com.devdowns.walletservice.domain.dto.BankDetails;
import com.devdowns.walletservice.domain.dto.CreateWalletTransactionRequest;
import com.devdowns.walletservice.domain.dto.SetBankDetailsResponse;
import com.devdowns.walletservice.domain.dto.TransactionCreatedResponse;
import com.devdowns.walletservice.domain.dto.TransactionRequestFilter;
import com.devdowns.walletservice.domain.dto.WalletTransactionHistory;
import com.devdowns.walletservice.infrastructure.inputport.BankAccountInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletInputPort;
import com.devdowns.walletservice.infrastructure.inputport.WalletTransactionInputPort;
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
  private final WalletTransactionInputPort walletTransactionInputPort;
  private final BankAccountInputPort bankAccountInputPort;

  public WalletAPI(WalletInputPort walletInputPort,
      WalletTransactionInputPort walletTransactionInputPort,
      BankAccountInputPort bankAccountInputPort) {
    this.walletInputPort = walletInputPort;
    this.walletTransactionInputPort = walletTransactionInputPort;
    this.bankAccountInputPort = bankAccountInputPort;
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
    List<WalletTransactionHistory> response = walletTransactionInputPort.getTransactionHistory(
        requestFilter);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping(value = "/transactions/bank")
  public ResponseEntity<SetBankDetailsResponse> setBankAccount(
      @RequestBody BankDetails bankDetails) {
    SetBankDetailsResponse response = bankAccountInputPort.setBankAccount(bankDetails);
    return ResponseEntity.ok().body(response);
  }

}
