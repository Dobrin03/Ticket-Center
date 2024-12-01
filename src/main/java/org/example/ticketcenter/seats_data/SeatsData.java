package org.example.ticketcenter.seats_data;

import java.math.BigDecimal;

public class SeatsData {
    private String type;
    private int quantity;
    private BigDecimal price;

    public String getType() {
        return type;
    }

    public void setName(String type) {
        this.type = type;
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

    public SeatsData(String type, int quantity, BigDecimal price) {
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }
}
