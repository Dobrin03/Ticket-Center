package org.example.ticketcenter.user_factory.models;

public class LoggedDistributor {
    private static LoggedDistributor instance;
    private Distributor distributor;

    public static LoggedDistributor getInstance(){
        if(instance==null){
            instance=new LoggedDistributor();
        }
        return instance;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }
}
