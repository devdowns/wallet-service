package com.devdowns.walletservice.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException() {
    super("Insufficient funds");
  }
}