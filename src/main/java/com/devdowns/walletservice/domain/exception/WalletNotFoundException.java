package com.devdowns.walletservice.domain.exception;

public class WalletNotFoundException extends RuntimeException {

  public WalletNotFoundException(Long userId) {
    super("Wallet not found for user with id: " + userId);
  }
}
