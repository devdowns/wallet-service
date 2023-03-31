package com.devdowns.walletservice.domain.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationData {

  private String name;
  private Account account;
}
