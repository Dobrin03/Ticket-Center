package org.example.ticketcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.common.Constants;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.user_factory.factories.UserFactory;

import java.io.IOException;
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
    private void onLogInButtonClick(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
        ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
        CloseSceneCommand close=new CloseSceneCommand(sceneAction);
        Invoker changeInvoker=new Invoker(change);
        Invoker closeInvoker=new Invoker(close);
        UserFactory userFactory=UserFactory.getInstance();
        DBConnection database= DBConnection.getInstance();
        CallableStatement statement;
        ResultSet resultSet = null;
        String user_column = null;
        String pass_column = null;
        String fxml = null;
        String query = null;

        if(!user_field.getText().isEmpty() && !pass_field.getText().isEmpty()) {
            if (radio_admin.isSelected()) {
                query="CALL FIND_ADMIN(?, ?, ?)";
                user_column="admin_user";
                pass_column="admin_pass";
                fxml= Constants.VIEW.ADMIN_WELCOME;
            } else if (radio_org.isSelected()) {
                query="CALL FIND_ORGANISER(?, ?, ?)";
                user_column="organiser_user";
                pass_column="organiser_pass";
                fxml=Constants.VIEW.ORGANISER_WELCOME;
            } else if (radio_distr.isSelected()) {
                query="CALL FIND_DISTRIBUTOR(?, ?, ?)";
                user_column="distributor_user";
                pass_column="distributor_pass";
                fxml=Constants.VIEW.DISTRIBUTOR_WELCOME;
            } else if (radio_cl.isSelected()) {
                query="CALL FIND_CLIENT(?, ?, ?)";
                user_column="client_user";
                pass_column="client_pass";
                fxml=Constants.VIEW.CLIENT_WELCOME;
            } else {
                lbl_error.setText("Please check the type of user to log in");
            }
        }
        else{
            lbl_error.setText("Please input the username and password");
        }

        if(!query.equals(null) && !user_column.equals(null) && !pass_column.equals(null) && !fxml.equals(null)){
            database.connect();
            statement = database.getConnection().prepareCall(query);

            statement.setString(1, user_field.getText());
            statement.setString(2, pass_field.getText());
            statement.registerOutParameter(3, OracleTypes.CURSOR);

            statement.execute();

            resultSet= (ResultSet) statement.getObject(3);

            if(resultSet.next()) {
                userFactory.setResult(resultSet);
                closeInvoker.execute(fxml, event);
                changeInvoker.execute(fxml, event);
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