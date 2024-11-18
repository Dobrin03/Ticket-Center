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
    private BigDecimal rating;

    public DistributorFactory(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            this.ID = resultSet.getInt("Distributor_ID");
            this.name = resultSet.getString("Distributor_Name");
            this.username = resultSet.getString("Distributor_User");
            this.password = resultSet.getString("Distributor_Pass");
            this.fee = resultSet.getBigDecimal("Distributor_Fee");
            this.rating = resultSet.getBigDecimal("Rating");
        }
    }

    @Override
    public void createUser() {
        Distributor distributor=Distributor.getInstance();
        distributor.setID(ID);
        distributor.setName(name);
        distributor.setUsername(username);
        distributor.setPassword(password);
        distributor.setFee(fee);
        distributor.setRating(rating);
    }
}
