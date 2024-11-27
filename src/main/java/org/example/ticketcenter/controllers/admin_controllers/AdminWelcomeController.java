package org.example.ticketcenter.controllers.admin_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Admin;

import java.io.IOException;
import java.sql.SQLException;

public class AdminWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private Admin admin;

    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException {
        admin= (Admin) UserFactory.getInstance().getUser();
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(admin.getName()).append("!");
        lbl_welcome.setText(builder.toString());
    }
    @FXML
    protected void onAddOrganiserClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/insertOrganiser.fxml", event);
    }

    @FXML
    protected void onAddDistributorClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/insertDistributor.fxml", event);
    }

    @FXML
    protected void onAddClientClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/insertClient.fxml", event);
    }

    @FXML
    protected void onEditUsersClick(ActionEvent event) throws IOException {
        changeScene.execute("/admin_fxml/editUser.fxml", event);
    }
    @FXML
    protected void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute("/log_in.fxml", event);
        closeScene.execute("", event);
    }
}
