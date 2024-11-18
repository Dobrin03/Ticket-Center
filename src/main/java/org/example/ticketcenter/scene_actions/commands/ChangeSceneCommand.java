package org.example.ticketcenter.scene_actions.commands;

import javafx.event.ActionEvent;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.interfaces.Command;

import java.io.IOException;

public class ChangeSceneCommand implements Command {
    private SceneActionsImplication sceneAction;

    public ChangeSceneCommand(SceneActionsImplication sceneAction){
        this.sceneAction=sceneAction;
    }
    @Override
    public void execute(String fxml, ActionEvent event) throws IOException {
        this.sceneAction.changeScene(fxml, event);
    }
}
