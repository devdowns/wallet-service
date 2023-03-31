package com.devdowns.walletservice.domain.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionCreatedResponse {

  @JsonProperty("wallet_transaction_id")
  private Long walletTransactionId;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("user_id")
  private Long userId;
}
