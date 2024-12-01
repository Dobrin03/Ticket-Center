package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UserFactory{
    private static UserFactory factoryInstance;
    private ResultSet result;

    public static UserFactory getInstance(){
        if (factoryInstance==null){
            factoryInstance=new UserFactory();
        }

        return factoryInstance;
    }

    public ResultSet getResult() {
        return result;
    }

    public void setResult(ResultSet result) {
        this.result = result;
    }

    public User getUser() throws SQLException, ClassNotFoundException {
        UserAbstractFactory abstractFactory = null;
        DBConnection connection=DBConnection.getInstance();
        connection.connect();
        ResultSetMetaData md= getResult().getMetaData();
        if(md.getColumnName(1).equalsIgnoreCase("Admin_ID")){
            abstractFactory=new AdminFactory(getResult());
        } else if (md.getColumnName(1).equalsIgnoreCase("Organiser_ID")) {
            abstractFactory=new OrganiserFactory(getResult());
        }
        else if (md.getColumnName(1).equalsIgnoreCase("Distributor_ID")) {
            abstractFactory=new DistributorFactory(getResult());
        }
        else if (md.getColumnName(1).equalsIgnoreCase("Client_ID")) {
            abstractFactory=new ClientFactory(getResult());
        }
        connection.closeConnection();

        return abstractFactory.createUser();
    }
}
