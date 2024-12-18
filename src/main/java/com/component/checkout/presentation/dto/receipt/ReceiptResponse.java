package com.component.checkout.presentation.dto.receipt;

public class ReceiptResponse {

    private final ReceiptDto receipt;
    private final boolean success;
    private final String message;

    private ReceiptResponse(Builder builder) {
        this.receipt = builder.receipt;
        this.success = builder.success;
        this.message = builder.message;
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

    public static class Builder {
        private ReceiptDto receipt;
        private boolean success;
        private String message;

        public Builder withReceipt(ReceiptDto receipt) {
            this.receipt = receipt;
            return this;
        }

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ReceiptResponse build() {
            return new ReceiptResponse(this);
        }
    }

    @Override
    public String toString() {
        return "ReceiptResponse{" +
                "receipt=" + receipt +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
