package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;

import java.io.IOException;

public class AddEventController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);

    @FXML
    protected void onRequestDistributorsClick(ActionEvent event) throws IOException {
        changeScene.execute("/organiser_fxml/request_distributors.fxml", event);
    }
}
