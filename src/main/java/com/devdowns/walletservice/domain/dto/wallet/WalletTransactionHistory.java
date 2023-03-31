package com.devdowns.walletservice.domain.dto.wallet;

import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.devdowns.walletservice.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
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
  private LocalDateTime date;
}
