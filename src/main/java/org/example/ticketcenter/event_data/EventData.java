package org.example.ticketcenter.event_data;

import java.sql.Date;
import java.time.LocalDate;

public class EventData {
    private int id;
    private String name;
    private int limit;
    private Date date;
    private String address;
    private String city;
    private String type;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public EventData(int id, String name, int limit, Date date, String address, String city, String type, String status) {
        this.id = id;
        this.name = name;
        this.limit = limit;
        this.date = date;
        this.address = address;
        this.city = city;
        this.type = type;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLimit() {
        return limit;
    }

    public Date getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getType() {
        return type;
    }

}
