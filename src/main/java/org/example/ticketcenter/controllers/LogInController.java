package org.example.ticketcenter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.example.ticketcenter.database.DBConnection;

import java.sql.*;

public class LogInController {
    @FXML
    private RadioButton radio_admin, radio_org, radio_distr, radio_cl;
    @FXML
    private Label lbl_error;
    @FXML
    private TextField user_field;
    @FXML
    private PasswordField pass_field;

    @FXML
    protected void onLogInButtonClick() throws SQLException, ClassNotFoundException {
        DBConnection database= DBConnection.getInstance();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        StringBuilder builder=new StringBuilder();
        String user_column = null;
        String pass_column = null;
        String table = null;

        if(!user_field.getText().isEmpty() && !pass_field.getText().isEmpty()) {
            if (radio_admin.isSelected()) {
                table="Administrator_Data";
                user_column="admin_user";
                pass_column="admin_pass";
            } else if (radio_org.isSelected()) {
                table="Organiser_Data";
                user_column="organiser_user";
                pass_column="organiser_pass";
            } else if (radio_distr.isSelected()) {
                table="Distributor_Data";
                user_column="distributor_user";
                pass_column="distributor_pass";
            } else if (radio_cl.isSelected()) {
                table="Client_Data";
                user_column="client_user";
                pass_column="client_pass";
            } else {
                lbl_error.setText("Please check the type of user to log in");
            }
        }
        else{
            lbl_error.setText("Please input the username and password");
        }

        if(table != null && user_column != null && pass_column != null){
            database.Connect();
            String query=builder.append("SELECT * FROM ").append(table).append(" WHERE ").append(user_column).append(" = ? AND ").append(pass_column).append(" = ?").toString();
            preparedStatement = database.getConnection().prepareStatement(query);

            preparedStatement.setString(1, user_field.getText());
            preparedStatement.setString(2, pass_field.getText());

            resultSet= preparedStatement.executeQuery();

            if(resultSet.next()) {
                lbl_error.setText("User is found");
            }
            else{
                lbl_error.setText("User is not found");
            }
            database.closeConnection();
        }
        else{
            lbl_error.setText("Failed to extract required data form database");
        }
    }
}