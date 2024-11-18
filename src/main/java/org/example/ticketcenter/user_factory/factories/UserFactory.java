package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFactory{
    public void getUser(ResultSet resultSet) throws SQLException {
        UserAbstractFactory abstractFactory = null;
        if(resultSet.findColumn("Admin_ID")==1){
            abstractFactory=new AdminFactory(resultSet);
        } else if (resultSet.findColumn("Organiser_ID")==1) {
            abstractFactory=new OrganiserFactory(resultSet);
        }
        else if (resultSet.findColumn("Distributor_ID")==1) {
            abstractFactory=new DistributorFactory(resultSet);
        }
        else if (resultSet.findColumn("Client_ID")==1) {
            abstractFactory=new ClientFactory(resultSet);
        }

        if(!abstractFactory.equals(null)) {
            abstractFactory.createUser();
        }
    }
}
