package org.example.ticketcenter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.ticketcenter.scene_actions.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.Invoker;
import org.example.ticketcenter.scene_actions.SceneActionsImplication;

import java.io.IOException;

public class AdminWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    @FXML
    protected void onAddOrganiserClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/insertOrganiser.fxml", event);
    }

    @FXML
    protected void onAddDistributorClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/insertDistributor.fxml", event);
    }

    @FXML
    protected void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute("/log_in.fxml", event);
        closeScene.execute("", event);
    }
}
