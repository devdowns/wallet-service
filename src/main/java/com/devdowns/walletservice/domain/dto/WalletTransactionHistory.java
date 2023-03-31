package com.devdowns.walletservice.domain.dto;

import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WalletTransactionHistory {

  @JsonProperty("transaction_id")
  private Long id;

  @JsonProperty("status")
  private TransactionStatus status;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("transaction_type")
  private TransactionType transactionType;

  @JsonProperty("date")
  private String date;
}
