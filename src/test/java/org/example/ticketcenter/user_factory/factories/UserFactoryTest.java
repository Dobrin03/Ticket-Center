package org.example.ticketcenter.user_factory.factories;

import javafx.application.Platform;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.Client;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {
    private UserFactory userFactory;
    private DBConnection connection;
    private CallableStatement stmt;
    private ResultSet result;

    @BeforeEach
    void setUp() throws SQLException {
        userFactory=UserFactory.getInstance();
        connection=DBConnection.getInstance();
    }

    @Test
    void testNoResultSet() throws SQLException, ClassNotFoundException {
        userFactory.setResult(null);
        assertThrows(NullPointerException.class, () -> {
            userFactory.getUser();
        });
    }

    @Test
    void testReturnAdmin() throws SQLException, ClassNotFoundException {
        connection.connect();
        stmt=connection.getConnection().prepareCall("CALL FIND_ADMIN(?, ?, ?)");
        stmt.setString(1, "project_admin");
        stmt.setString(2, "admin123");
        stmt.registerOutParameter(3, OracleTypes.CURSOR);
        stmt.execute();

        result= (ResultSet) stmt.getObject(3);

        while (result.next()){
            userFactory.setResult(result);
            assertInstanceOf(Admin.class, userFactory.getUser());
        }

        connection.closeConnection();
    }

    @Test
    void testReturnOrganiser() throws SQLException, ClassNotFoundException {
        connection.connect();
        stmt=connection.getConnection().prepareCall("CALL FIND_ORGANISER(?, ?, ?)");
        stmt.setString(1, "john_lenon");
        stmt.setString(2, "Johnathan123");
        stmt.registerOutParameter(3, OracleTypes.CURSOR);
        stmt.execute();

        result= (ResultSet) stmt.getObject(3);

        while (result.next()){
            userFactory.setResult(result);
            assertInstanceOf(Organiser.class, userFactory.getUser());
        }

        connection.closeConnection();
    }

    @Test
    void testReturnDistributor() throws SQLException, ClassNotFoundException {
        Platform.startup(() -> {});
        connection.connect();
        stmt=connection.getConnection().prepareCall("CALL FIND_DISTRIBUTOR(?, ?, ?)");
        stmt.setString(1, "exp_share");
        stmt.setString(2, "ShareIsCare");
        stmt.registerOutParameter(3, OracleTypes.CURSOR);
        stmt.execute();

        result= (ResultSet) stmt.getObject(3);

        while (result.next()){
            userFactory.setResult(result);
            assertInstanceOf(Distributor.class, userFactory.getUser());
        }

        connection.closeConnection();
    }

    @Test
    void testReturnClient() throws SQLException, ClassNotFoundException {
        connection.connect();
        stmt=connection.getConnection().prepareCall("CALL FIND_CLIENT(?, ?, ?)");
        stmt.setString(1, "vankata");
        stmt.setString(2, "Vankata23");
        stmt.registerOutParameter(3, OracleTypes.CURSOR);
        stmt.execute();

        result= (ResultSet) stmt.getObject(3);

        while (result.next()){
            userFactory.setResult(result);
            assertInstanceOf(Client.class, userFactory.getUser());
        }

        connection.closeConnection();
    }
}