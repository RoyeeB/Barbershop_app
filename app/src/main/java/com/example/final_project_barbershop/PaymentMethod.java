package com.example.final_project_barbershop;

import java.util.Date;

public class PaymentMethod {

        private String cardNumber;
        private String expiryDate;
        private String CVV;
        private String id;


        // Constructor
        public PaymentMethod(String cardNumber, String expiryDate, String CVV ,String id) {
            this.cardNumber = cardNumber;
            this.expiryDate = expiryDate;
            this.CVV = CVV;
            this.id = id;
        }

        // Empty constructor (required for Firestore)
        public PaymentMethod() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}



