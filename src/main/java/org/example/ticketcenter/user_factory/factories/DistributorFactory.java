package org.example.ticketcenter.user_factory.factories;

import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.interfaces.UserAbstractFactory;
import org.example.ticketcenter.user_factory.models.Distributor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DistributorFactory implements UserAbstractFactory {
    private int ID;
    private String name;
    private String username;
    private String password;
    private BigDecimal fee;

    public DistributorFactory(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt("Distributor_ID");
        this.name = resultSet.getString("Distributor_Name");
        this.username = resultSet.getString("Distributor_User");
        this.password = resultSet.getString("Distributor_Pass");
        this.fee = resultSet.getBigDecimal("Distributor_Fee");
    }

    @Override
    public User createUser() {
        return new Distributor(ID, name, username, password, fee);
    }
}
