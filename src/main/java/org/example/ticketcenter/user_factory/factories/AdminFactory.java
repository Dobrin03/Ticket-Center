package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;
import org.example.ticketcenter.user_factory.models.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminFactory implements UserAbstractFactory {
    private int ID;
    private String name;
    private String username;
    private String password;

    public AdminFactory(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt("Admin_ID");
        this.name = resultSet.getString("Admin_Name");
        this.username = resultSet.getString("Admin_User");
        this.password = resultSet.getString("Admin_Pass");
    }
    @Override
    public User createUser() {
        return new Admin(ID, name, username, password);
    }
}
