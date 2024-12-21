package org.example.ticketcenter.controllers.admin_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.ticketcenter.common.Constants;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.LoggedAdmin;

import java.io.IOException;
import java.sql.SQLException;

public class AdminWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private LoggedAdmin admin;

    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        admin=LoggedAdmin.getInstance();
        admin.setAdmin((Admin) UserFactory.getInstance().getUser());
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(admin.getAdmin().getName()).append("!");
        lbl_welcome.setText(builder.toString());
    }
    @FXML
    private void onAddOrganiserClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.INSERT_ORGANISER, event);
    }

    @FXML
    private void onAddDistributorClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.INSERT_DISTRIBUTOR, event);
    }

    @FXML
    private void onAddClientClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.INSERT_CLIENT, event);
    }

    @FXML
    private void onEditUsersClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.EDIT_USER, event);
    }
    @FXML
    private void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.LOG_IN, event);
        closeScene.execute("", event);
    }
}
