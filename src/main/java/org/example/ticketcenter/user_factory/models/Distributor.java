package org.example.ticketcenter.user_factory.models;

import org.example.ticketcenter.user_factory.interfaces.User;

import java.math.BigDecimal;

public class Distributor extends User {
    private static Distributor distributorInstance;
    private int ID;
    private String name;
    private String username;
    private String password;

    private BigDecimal fee;
    private BigDecimal rating;

    public static Distributor getInstance() {
        if(distributorInstance==null){
            distributorInstance=new Distributor();
        }

        return distributorInstance;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
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

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getRating() {
        return rating;
    }
}
