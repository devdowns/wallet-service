package com.devdowns.walletservice.domain.exception;

public class MalformedRequestException extends RuntimeException {

  public MalformedRequestException(String msg) {
    super(msg);
  }
}
