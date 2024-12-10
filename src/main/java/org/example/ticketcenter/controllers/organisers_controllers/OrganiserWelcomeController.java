package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.sql.SQLException;

public class OrganiserWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private Organiser organiser;

    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        organiser= (Organiser) UserFactory.getInstance().getUser();
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(organiser.getName()).append("!");
        lbl_welcome.setText(builder.toString());
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
