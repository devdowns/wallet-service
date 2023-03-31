package com.devdowns.walletservice.domain.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

  @JsonProperty("source")
  private SourceData sourceData;

  @JsonProperty("destination_data")
  private DestinationData destinationData;

  @JsonProperty("amount")
  private BigDecimal amount;
}
