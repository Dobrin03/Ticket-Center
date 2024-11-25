package org.example.ticketcenter.user_factory.models;

import org.example.ticketcenter.user_factory.interfaces.User;

public class Client extends User {
    private int ID;
    private String name;
    private String username;
    String password;
    private String email;
    private String address;
    private String number;
    private String city;

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Client(int ID, String name, String username, String password, String email, String address, String number, String city) {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.number = number;
        this.city = city;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }
}
