package org.example.ticketcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertOrganiserController {
    @FXML
    private TextField name_field, user_field;
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
        PreparedStatement preparedStatement=database.getConnection().
                prepareStatement("CALL Organiser_Ins(?, ?, ?)");
        if(!name_field.getText().isEmpty() && !user_field.getText().isEmpty() &&
                !pass_field.getText().isEmpty() && !confirm_field.getText().isEmpty()){
            if(pass_field.getText().equals(confirm_field.getText())){
                preparedStatement.setString(1, name_field.getText());
                preparedStatement.setString(2, user_field.getText());
                preparedStatement.setString(3, pass_field.getText());

                preparedStatement.execute();
                lbl_error.setText("User added successfully");

                name_field.clear();
                user_field.clear();
                pass_field.clear();
                confirm_field.clear();
            }
            else{
                lbl_error.setText("Not matching password");
            }
        }
        else{
            lbl_error.setText("Please fill all spaces");
        }
        database.closeConnection();
    }
}
