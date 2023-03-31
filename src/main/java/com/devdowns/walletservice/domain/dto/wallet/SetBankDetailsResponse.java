package com.devdowns.walletservice.domain.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class SetBankDetailsResponse {

  @JsonProperty("holder_name")
  String holderName;

  @JsonProperty("account_number")
  String accountNumber;

  @JsonProperty("routing_number")
  String routingNumber;

  @JsonProperty("bank_name")
  String bankName;
}


