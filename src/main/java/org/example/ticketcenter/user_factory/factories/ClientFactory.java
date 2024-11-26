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
    private String city;


    public ClientFactory(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt("Client_ID");
        this.name = resultSet.getString("Client_Name");
        this.username = resultSet.getString("Client_User");
        this.password = resultSet.getString("Client_Pass");
        this.email = resultSet.getString("Client_Email");
        this.address = resultSet.getString("Client_Address");
        this.number = resultSet.getString("Client_Number");
        this.city = resultSet.getString("City_name");

    }

    @Override
    public User createUser() {
        return new Client(ID, name, username, password, email, address, number, city);
    }
}
