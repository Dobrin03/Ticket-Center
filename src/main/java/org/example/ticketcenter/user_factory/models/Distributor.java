package org.example.ticketcenter.user_factory.models;

import javafx.scene.control.CheckBox;
import org.example.ticketcenter.user_factory.interfaces.User;

import java.math.BigDecimal;

public class Distributor extends User {
    private int ID;
    private String name;
    private String username;
    private String password;
    private BigDecimal fee;
    private BigDecimal rating;
    private CheckBox add;

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

    public CheckBox getAdd() {
        return add;
    }

    public void setAdd(CheckBox add) {
        this.add = add;
    }

    public Distributor(int ID, String name, String username, String password, BigDecimal fee) {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.fee = fee;
        this.add=new CheckBox();
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
