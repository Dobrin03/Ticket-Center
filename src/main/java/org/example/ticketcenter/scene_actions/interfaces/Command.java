package org.example.ticketcenter.scene_actions.interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface Command {
    void execute(String fxml, ActionEvent event) throws IOException;
}
