package com.devdowns.walletservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
  COMPLETED("Transaction completed"),

  FAILED("Transaction failed");

  private final String message;
}
