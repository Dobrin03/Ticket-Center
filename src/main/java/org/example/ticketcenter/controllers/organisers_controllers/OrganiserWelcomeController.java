package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.LoggedOrganiser;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganiserWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private LoggedOrganiser organiser;

    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        organiser= LoggedOrganiser.getInstance();
        organiser.setOrganiser((Organiser) UserFactory.getInstance().getUser());
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(organiser.getOrganiser().getName()).append("!");
        lbl_welcome.setText(builder.toString());

        DBConnection connection=DBConnection.getInstance();
        connection.connect();

        CallableStatement stmt=connection.getConnection().prepareCall("CALL NO_BOUGHT_TICKETS_ORG(? ,?)");
        stmt.setInt(1, organiser.getOrganiser().getID());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet result= (ResultSet) stmt.getObject(2);

        if(result.isBeforeFirst()){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event with no reservations");
            alert.setHeaderText("There are events with no reservations:");

            while(result.next()){
                alert.setContentText(alert.getContentText()+", "+result.getString("Event_Name"));
            }

            alert.showAndWait();
        }
    }

    @FXML
    protected void onAddEventClick(ActionEvent event) throws IOException {
        changeScene.execute("/organiser_fxml/add_event.fxml", event);
    }
    @FXML
    protected void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute("/log_in.fxml", event);
        closeScene.execute("", event);
    }
    @FXML
    private void onEditEventClick(ActionEvent event) throws IOException {
        changeScene.execute("/organiser_fxml/edit_event.fxml", event);
    }

    @FXML
    private void onRateDistributorClick(ActionEvent event) throws IOException {
        changeScene.execute("/organiser_fxml/rateDistributor.fxml", event);
    }
}
