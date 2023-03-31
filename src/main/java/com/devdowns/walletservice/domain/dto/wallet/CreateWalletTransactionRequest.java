package com.devdowns.walletservice.domain.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateWalletTransactionRequest {

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("user_id")
  private Long userId;
}
