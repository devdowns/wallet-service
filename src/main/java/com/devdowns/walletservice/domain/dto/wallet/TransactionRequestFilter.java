package com.devdowns.walletservice.domain.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRequestFilter {

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("page")
  private int page = 0;

  @JsonProperty("size")
  private int size = 10;

  @JsonProperty("min_amount")
  private BigDecimal minAmount;

  @JsonProperty("max_amount")
  private BigDecimal maxAmount;

  @JsonProperty("start_date")
  private LocalDate startDate;

  @JsonProperty("end_date")
  private LocalDate endDate;
}
