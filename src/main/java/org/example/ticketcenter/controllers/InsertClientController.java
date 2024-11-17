package org.example.ticketcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.Invoker;
import org.example.ticketcenter.scene_actions.SceneActionsImplication;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertClientController {
    @FXML
    private TextField name_field, user_field, email_field, city_field, address_field, number_field;
    @FXML
    private PasswordField pass_field, confirm_field;
    @FXML
    private Label lbl_error;

    @FXML
    protected void onCancelClick(ActionEvent event) throws IOException {
        SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
        CloseSceneCommand close=new CloseSceneCommand(sceneAction);
        Invoker closeScene=new Invoker(close);
        closeScene.execute("", event);
    }

    @FXML
    protected void onAddClick(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        DBConnection database=DBConnection.getInstance();
        database.connect();
        PreparedStatement clientInsStmt=database.getConnection().
                prepareStatement("CALL Client_Ins(?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement cityInsStmt=database.getConnection().
                prepareStatement("CALL City_Ins(?)");
        PreparedStatement checkCityQuery=database.getConnection().
                prepareStatement("SELECT City_ID FROM City WHERE City_Name=?");
        ResultSet result;
        int id = 0;
        if(!name_field.getText().isEmpty() && !user_field.getText().isEmpty()
                && !email_field.getText().isEmpty() && !city_field.getText().isEmpty()
                && !address_field.getText().isEmpty() && !number_field.getText().isEmpty() &&
                !pass_field.getText().isEmpty() && !confirm_field.getText().isEmpty()){
            checkCityQuery.setString(1, city_field.getText());
            result= checkCityQuery.executeQuery();

            if(!result.isBeforeFirst()){
                cityInsStmt.setString(1, city_field.getText());
                cityInsStmt.execute();
                result= checkCityQuery.executeQuery();
            }

            while(result.next()){
                id=result.getInt("City_ID");
            }

            if(number_field.getText().length()==10 && number_field.getText().matches("[0-9]*")) {
                if(id!=0) {
                    if (pass_field.getText().equals(confirm_field.getText())) {
                        clientInsStmt.setString(1, name_field.getText());
                        clientInsStmt.setString(2, user_field.getText());
                        clientInsStmt.setString(3, pass_field.getText());
                        clientInsStmt.setString(4, email_field.getText());
                        clientInsStmt.setString(5, address_field.getText());
                        clientInsStmt.setString(6, number_field.getText());
                        clientInsStmt.setInt(7, id);

                        clientInsStmt.execute();
                        lbl_error.setText("User added successfully");

                        name_field.clear();
                        user_field.clear();
                        email_field.clear();
                        city_field.clear();
                        number_field.clear();
                        address_field.clear();
                        pass_field.clear();
                        confirm_field.clear();
                    } else {
                        lbl_error.setText("Not matching password");
                    }
                }
                else{
                    lbl_error.setText("Unable to add or find city");
                }
            }
            else{
                lbl_error.setText("Insert a valid number");
            }
        }
        else{
            lbl_error.setText("Please fill all spaces");
        }
        database.closeConnection();
    }
}
