package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganiserFactory implements UserAbstractFactory {
    private int ID;
    private String name;
    private String username;
    String password;

    public OrganiserFactory(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            this.ID = resultSet.getInt("Organiser_ID");
            this.name = resultSet.getString("Organiser_Name");
            this.username = resultSet.getString("Organiser_User");
            this.password = resultSet.getString("Organiser_Pass");
        }
    }

    @Override
    public void createUser() {
        Organiser organiser=Organiser.getInstance();
        organiser.setID(ID);
        organiser.setName(name);
        organiser.setUsername(username);
        organiser.setPassword(password);
    }
}
