package org.example.ticketcenter.user_factory.models;

public class LoggedAdmin {
    private static LoggedAdmin instance;
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public static LoggedAdmin getInstance(){
        if(instance==null){
            instance=new LoggedAdmin();
        }
        return instance;
    }
}
