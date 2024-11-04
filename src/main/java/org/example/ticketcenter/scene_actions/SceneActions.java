package org.example.ticketcenter.scene_actions;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface SceneActions {
    void changeScene(String fxml, ActionEvent event) throws IOException;
    void closeScene(ActionEvent event);
}
