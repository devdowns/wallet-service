package com.devdowns.walletservice.domain.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

  private String accountNumber;
  private String currency;
  private String routingNumber;
}
