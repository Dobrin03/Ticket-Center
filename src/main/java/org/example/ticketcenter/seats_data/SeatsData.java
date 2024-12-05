package org.example.ticketcenter.seats_data;

import java.math.BigDecimal;

public class SeatsData {
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private int quantity;
    private BigDecimal price;

    public String getType() {
        return type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public SeatsData(int ID, String type, int quantity, BigDecimal price) {
        this.ID = ID;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }
}