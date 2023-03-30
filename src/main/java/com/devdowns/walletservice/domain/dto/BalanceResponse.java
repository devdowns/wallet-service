package com.devdowns.walletservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BalanceResponse {

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("user_id")
  private Long userId;
}
