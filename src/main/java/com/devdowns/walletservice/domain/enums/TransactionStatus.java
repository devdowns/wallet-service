package com.devdowns.walletservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionStatus {
  PROCESSING("Transaction in progess"),

  COMPLETED("Transaction completed"),

  REFUNDED("Refunded to your wallet"),

  FAILED("Transaction failed");

  private final String message;
}
