package com.devdowns.walletservice.domain.dto.payment;

import com.devdowns.walletservice.domain.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record PaymentResponse(@JsonProperty("status") TransactionStatus status,
                              @JsonProperty("date") LocalDateTime date) {

}
