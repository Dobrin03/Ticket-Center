package org.example.ticketcenter.scene_actions;

import javafx.event.ActionEvent;

import java.io.IOException;

public class Invoker {
    public Command command;

    public Invoker(Command command){
        this.command=command;
    }

    public void execute(String fxml, ActionEvent event) throws IOException {
        command.execute(fxml, event);
    }
}
