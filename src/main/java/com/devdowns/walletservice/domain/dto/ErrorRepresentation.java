package com.devdowns.walletservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorRepresentation(
    @JsonProperty("code") String code,
    @JsonProperty("message") String message) {

}
