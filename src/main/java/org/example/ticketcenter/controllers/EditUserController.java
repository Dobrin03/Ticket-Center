package org.example.ticketcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.user_factory.interfaces.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EditUserController {
    @FXML
    private TableView<User> user_view;
    private String update;
    private DBConnection connection;
    private ObservableList<User>users;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        connection=DBConnection.getInstance();
        connection.connect();
        String search ="SELECT * FROM Organiser_Data";
        Statement query=connection.getConnection().createStatement();
        ResultSet result= query.executeQuery(search);
        ResultSetMetaData rd= result.getMetaData();
        List<User> obUser=((List<User>) result.toList())
        users= FXCollections.observableArrayList();
        users.clear();
        users.addAll((List<User>)result.toList

    }

}
