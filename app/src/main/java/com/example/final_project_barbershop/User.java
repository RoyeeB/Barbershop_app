package com.example.final_project_barbershop;

import java.util.ArrayList;

public class User {

    private String email;
    private String name;
    private String password;
    private String phone;
    private ArrayList <Appointment> myAppointments;
    private PaymentMethod PM;

    public User(){}

    // ללא אמצעי תשלום כי לאחר ההרשמה יש אפשרות להוסיף - לא חייב
    public User(String name, String email, String phone ,String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPaymentMethod(PaymentMethod PM) {
        this.PM = PM;
    }

    public PaymentMethod getPM() {
        return PM;
    }

    public void setPM(PaymentMethod PM) {
        this.PM = PM;
    }

    public ArrayList<Appointment> getMyAppointments() {
        return myAppointments;
    }
}

