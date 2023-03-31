package com.devdowns.walletservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
  INVALID_USER("user not found"),

  INSUFFICIENT_BALANCE("insufficient balance in wallet"),

  /**
   * Replace for your custom message based on expected parameters
   */
  INVALID_BODY(""),

  WALLET_NOT_FOUND("wallet not found"),

  BANKING_DETAILS_NOT_SET("no banking details found"),

  GENERIC_ERROR("yikes, something bad happened");

  private final String message;
}
