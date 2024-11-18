package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;
import org.example.ticketcenter.user_factory.models.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientFactory implements UserAbstractFactory {
    private int ID;
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
    private String number;
    private int city;


    public ClientFactory(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            this.ID = resultSet.getInt("Client_ID");
            this.name = resultSet.getString("Client_Name");
            this.username = resultSet.getString("Client_User");
            this.password = resultSet.getString("Client_Pass");
            this.email = resultSet.getString("Client_Email");
            this.address = resultSet.getString("Client_Address");
            this.number = resultSet.getString("Client_Number");
            this.city = resultSet.getInt("City_ID");
        }
    }

    @Override
    public void createUser() {
        Client client=Client.getInstance();
        client.setID(ID);
        client.setName(name);
        client.setUsername(username);
        client.setPassword(password);
        client.setEmail(email);
        client.setAddress(address);
        client.setNumber(number);
        client.setCity(city);
    }
}
