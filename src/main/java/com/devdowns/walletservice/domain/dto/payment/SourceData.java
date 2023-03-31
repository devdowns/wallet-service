package com.devdowns.walletservice.domain.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceData {

  private String type;
  private SourceInformation sourceInformation;
  private Account account;
}
