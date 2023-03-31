package com.devdowns.walletservice.domain.exception;

public class BankNotFoundException extends RuntimeException {

  public BankNotFoundException() {
    super("Bank not found");
  }
}
