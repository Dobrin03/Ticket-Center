package org.example.ticketcenter.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.example.ticketcenter.common.Constants;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;

public class ProgramApplication extends Application {
    private static Logger logger=Logger.getLogger(ProgramApplication.class);
    @Override
    public void start(Stage stage) throws IOException {
        PropertyConfigurator.configure( getClass().getResource(Constants.CONFIGURATION.LOG4J_PROPERTIES));
        URL path=getClass().getResource(Constants.VIEW.LOG_IN);
        if(path!=null) {
            FXMLLoader fxmlLoader = new FXMLLoader(path);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }else{
            logger.error("Couldn't load the main fxml");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}