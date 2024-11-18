package org.example.ticketcenter.user_factory.models;

import org.example.ticketcenter.user_factory.interfaces.User;

public class Admin extends User {
    private static Admin adminInstance;
    private int ID;
    private String name;
    private String username;
    String password;

    public static Admin getInstance() {
        if(adminInstance==null){
            adminInstance=new Admin();
        }

        return adminInstance;
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
}
