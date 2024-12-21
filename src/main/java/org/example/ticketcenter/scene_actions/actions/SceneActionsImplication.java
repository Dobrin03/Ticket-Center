package org.example.ticketcenter.scene_actions.actions;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.example.ticketcenter.common.Constants;
import org.example.ticketcenter.scene_actions.interfaces.SceneActions;

import java.io.IOException;
import java.util.Objects;

public class SceneActionsImplication implements SceneActions {
    private static Logger logger=Logger.getLogger(SceneActionsImplication.class);
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static SceneActionsImplication actionsInstance;

    public static SceneActionsImplication getInstance(){
        if(actionsInstance==null){
            actionsInstance=new SceneActionsImplication();
        }

        return actionsInstance;
    }
    @Override
    public void changeScene(String fxml, ActionEvent event) throws IOException {
        if(fxml!=null) {
            root = FXMLLoader.load(getClass().getResource(fxml));
            stage = new Stage();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }else{
            logger.error("Couldn't open fxml file");
        }
    }

    @Override
    public void closeScene(ActionEvent event) {
        stage=(Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
