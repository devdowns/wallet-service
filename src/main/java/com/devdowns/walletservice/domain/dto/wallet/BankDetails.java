package com.devdowns.walletservice.domain.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDetails {

  @JsonProperty("name")
  private String name;

  @JsonProperty("surname")
  private String surname;

  @JsonProperty("national_id")
  private String nationalId;

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("routing_number")
  private String routingNumber;

  @JsonProperty("bank_name")
  private String bankName;
}
