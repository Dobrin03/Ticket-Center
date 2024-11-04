package org.example.ticketcenter.scene_actions;

import javafx.event.ActionEvent;

import java.io.IOException;

public class CloseSceneCommand implements Command{
    private SceneActionsImplication sceneAction;

    public CloseSceneCommand(SceneActionsImplication sceneAction){
        this.sceneAction=sceneAction;
    }
    @Override
    public void execute(String fxml, ActionEvent event) throws IOException {
        this.sceneAction.closeScene(event);
    }
}
