package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.interfaces.User;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestDistributorsController {
    @FXML
    private TableView<User> distributors_view;

    @FXML
    private TableColumn<User, Integer> col_id;

    @FXML
    private TableColumn<User, String> col_name;

    @FXML
    private TableColumn<User, BigDecimal> col_fee;

    @FXML
    private TableColumn<User, BigDecimal> col_rating;
    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {

        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("ID"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col_fee.setCellValueFactory(new PropertyValueFactory<User, BigDecimal>("fee"));
        col_rating.setCellValueFactory(new PropertyValueFactory<User, BigDecimal>("rating"));

        UserFactory userFactory= UserFactory.getInstance();
        String query= "CALL FIND_ALL_DISTRIBUTORS(?)";
        DBConnection connection= DBConnection.getInstance();
        connection.connect();
        users.clear();
        CallableStatement statement=connection.getConnection().prepareCall(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.registerOutParameter(1, OracleTypes.CURSOR);
        statement.execute();
        ResultSet result = (ResultSet) statement.getObject(1);

        while(result.next()){
            userFactory.setResult(result);
            users.add(userFactory.getUser());
        }

        distributors_view.setItems(users);
        connection.closeConnection();
    }
}
