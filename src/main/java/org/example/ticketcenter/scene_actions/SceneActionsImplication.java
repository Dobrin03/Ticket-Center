package org.example.ticketcenter.scene_actions;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneActionsImplication implements SceneActions {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String url = "/org/example/ticketcenter/presentation/views";

    @Override
    public void changeScene(String fxml, ActionEvent event) throws IOException {
        StringBuilder builder= new StringBuilder();
        url=builder.append(url).append(fxml).toString();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(url)));
        stage = new Stage();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @Override
    public void closeScene(ActionEvent event) {
        stage=(Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
