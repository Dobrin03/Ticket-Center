package org.example.ticketcenter.controllers.distributors_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;

import java.io.IOException;
import java.sql.SQLException;

public class DistributorWelcomeController {

    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private Distributor distributor;

    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        distributor= (Distributor) UserFactory.getInstance().getUser();
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(distributor.getName()).append("!");
        lbl_welcome.setText(builder.toString());
    }

    @FXML
    protected void onCheckRequestsClick(ActionEvent event) throws IOException {
        changeScene.execute("/distributor_fxml/checkRequests.fxml", event);
    }
    @FXML
    protected void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute("/log_in.fxml", event);
        closeScene.execute("", event);
    }
}
