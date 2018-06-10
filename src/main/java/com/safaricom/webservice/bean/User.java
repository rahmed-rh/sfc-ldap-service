package com.safaricom.webservice.bean;

public class User {
    String username;//givenname
    String fullname;//sn
    String mail;//mail
    String telephone;//telephonenumber

    public User(String username, String fullname, String mail, String telephone) {
        this.username = username;
        this.fullname = fullname;
        this.mail = mail;
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
