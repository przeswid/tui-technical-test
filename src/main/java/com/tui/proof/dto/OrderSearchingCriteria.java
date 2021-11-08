package com.tui.proof.dto;

import lombok.Getter;

@Getter
public final class OrderSearchingCriteria {

    private final String clientEmail;

    private final String clientFirstName;

    private final String clientLastName;

    private OrderSearchingCriteria(Builder builder) {
        this.clientEmail = builder.getClientEmail();
        this.clientFirstName = builder.getClientFirstName();
        this.clientLastName = builder.getClientLastName();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Getter
    public static class Builder {

        private String clientEmail;

        private String clientFirstName;

        private String clientLastName;

        public Builder clientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public Builder clientFirstName(String clientFirstName) {
            this.clientFirstName = clientFirstName;
            return this;
        }

        public Builder clientLastName(String clientLastName) {
            this.clientLastName = clientLastName;
            return this;
        }

        public OrderSearchingCriteria build() {
            return new OrderSearchingCriteria(this);
        }
    }
}
