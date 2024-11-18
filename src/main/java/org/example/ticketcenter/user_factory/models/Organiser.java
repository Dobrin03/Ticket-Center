package org.example.ticketcenter.user_factory.models;

import org.example.ticketcenter.user_factory.interfaces.User;

public class Organiser extends User {
    private static Organiser organiserInstance;
    private int ID;
    private String name;
    private String username;
    String password;

    public static Organiser getInstance() {
        if(organiserInstance==null){
            organiserInstance=new Organiser();
        }

        return organiserInstance;
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
