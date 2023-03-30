package com.devdowns.walletservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
  INVALID_USER("user not found"),

  INSUFFICIENT_BALANCE("insufficient balance in wallet"),

  INVALID_BODY("replace for your custom message"),

  WALLET_NOT_FOUND("wallet not found"),

  GENERIC_ERROR("yikes, something bad happened");

  private final String message;
}
