package com.example.final_project_barbershop;

public class Appointment {

    private String date;
    private String time;
//    private int cardCash;
//    private int rate;


    public Appointment(){}

    public Appointment(String date , String time , int cardCash){
        this.date = date;
        this.time = time;
//        this.cardCash = cardCash;
    }


//    public int getRate() {
//        return rate;
//    }
//
//    public void setRate(int rate) {
//        this.rate = rate;
//    }
//
//    public int getCardCash() {
//        return cardCash;
//    }
//
//    public void setCardCash(int cardCash) {
//        this.cardCash = cardCash;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
