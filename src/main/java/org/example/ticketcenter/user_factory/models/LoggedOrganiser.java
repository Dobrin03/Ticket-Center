package org.example.ticketcenter.user_factory.models;

public class LoggedOrganiser {
    private static LoggedOrganiser instance;
    private Organiser organiser;

    public static  LoggedOrganiser getInstance(){
        if(instance==null){
            instance=new LoggedOrganiser();
        }
        return instance;
    }

    public Organiser getOrganiser() {
        return organiser;
    }

    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }
}
