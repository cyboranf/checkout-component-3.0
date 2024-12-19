package com.component.checkout.presentation.dto.receipt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReceiptResponse {

    private final ReceiptDto receipt;
    private final boolean success;
    private final String message;

    @JsonCreator
    public ReceiptResponse(
            @JsonProperty("receipt") ReceiptDto receipt,
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {
        this.receipt = receipt;
        this.success = success;
        this.message = message;
    }

    public ReceiptDto getReceipt() {
        return receipt;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
