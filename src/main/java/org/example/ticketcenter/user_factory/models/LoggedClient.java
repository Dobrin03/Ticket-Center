package org.example.ticketcenter.user_factory.models;

public class LoggedClient {
    private static LoggedClient instance;
    private Client client;

    public static LoggedClient getInstance(){
        if(instance==null){
            instance=new LoggedClient();
        }
        return instance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
