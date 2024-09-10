package com.example.squareapi.model;

public class CheckoutRequest {

    private Checkout checkout;

    public CheckoutRequest(String deviceId) {
        this.checkout = new Checkout(deviceId);
    }

    public static class Checkout {
        private AmountMoney amount_money;
        private DeviceOptions device_options;
        private String note = "ice cream";

        public Checkout(String deviceId) {
            this.amount_money = new AmountMoney(100, "USD");
            this.device_options = new DeviceOptions(deviceId);
        }

        // Inner classes for AmountMoney and DeviceOptions
        public static class AmountMoney {
            private int amount;
            private String currency;

            public AmountMoney(int amount, String currency) {
                this.amount = amount;
                this.currency = currency;
            }
        }

        public static class DeviceOptions {
            private String device_id;
            private boolean skip_receipt_screen = false;
            private TipSettings tip_settings = new TipSettings();

            public DeviceOptions(String device_id) {
                this.device_id = device_id;
            }

            public static class TipSettings {
                private boolean allow_tipping = true;
                private boolean custom_tip_field = false;
                private boolean separate_tip_screen = true;
            }
        }
    }
}
