package com.devdowns.walletservice.domain.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionStatusTest {

  @Test
  public void testEnumValues() {
    Assertions.assertEquals("Transaction in progess", TransactionStatus.PROCESSING.getMessage());
    Assertions.assertEquals("Transaction completed", TransactionStatus.COMPLETED.getMessage());
    Assertions.assertEquals("Refunded to your wallet", TransactionStatus.REFUNDED.getMessage());
    Assertions.assertEquals("Transaction failed", TransactionStatus.FAILED.getMessage());
  }
}
