package com.devdowns.walletservice.domain.advice;

import com.devdowns.walletservice.domain.dto.wallet.ErrorRepresentation;
import com.devdowns.walletservice.domain.enums.ErrorCodes;
import com.devdowns.walletservice.domain.exception.InsufficientBalanceException;
import com.devdowns.walletservice.domain.exception.MalformedRequestException;
import com.devdowns.walletservice.domain.exception.UserNotFoundException;
import com.devdowns.walletservice.domain.exception.WalletNotFoundException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CentralizedExceptionHandler {

  @ExceptionHandler(WalletNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorRepresentation handle(WalletNotFoundException ex) {
    return new ErrorRepresentation(
        ErrorCodes.WALLET_NOT_FOUND.name(),
        ErrorCodes.WALLET_NOT_FOUND.getMessage()
    );
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorRepresentation handle(InsufficientBalanceException ex) {
    return new ErrorRepresentation(
        ErrorCodes.INSUFFICIENT_BALANCE.name(),
        ErrorCodes.INSUFFICIENT_BALANCE.getMessage()
    );
  }

  @ExceptionHandler(MalformedRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorRepresentation handle(MalformedRequestException ex) {
    return new ErrorRepresentation(
        ErrorCodes.INVALID_BODY.name(),
        ex.getMessage()
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorRepresentation handle(UserNotFoundException ex) {
    return new ErrorRepresentation(
        ErrorCodes.INVALID_USER.name(),
        ErrorCodes.INVALID_USER.getMessage()
    );
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorRepresentation handle(InternalServerErrorException ex) {
    return new ErrorRepresentation(
        ErrorCodes.GENERIC_ERROR.name(),
        ErrorCodes.GENERIC_ERROR.getMessage()
    );
  }

}
