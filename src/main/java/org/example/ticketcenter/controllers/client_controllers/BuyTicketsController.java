package org.example.ticketcenter.controllers.client_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.seats_data.SeatsData;

import java.io.IOException;
import java.math.BigDecimal;

public class BuyTicketsController {
    @FXML
    private Label lbl_name, lbl_type, lbl_city, lbl_date, lbl_limit, lbl_status, lbl_organiser;
    @FXML
    private ComboBox<String> cb_distributors;
    @FXML
    private TableView<SeatsData> seat_view;
    @FXML
    private TableColumn<SeatsData, String> col_type;
    @FXML
    private TableColumn<SeatsData, Integer> col_available;
    @FXML
    private TableColumn<SeatsData, BigDecimal> col_price;
    @FXML
    private TableColumn<SeatsData, Integer> col_reserve;

    @FXML
    protected void onCancelClick(ActionEvent event) throws IOException {
        SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
        CloseSceneCommand close=new CloseSceneCommand(sceneAction);
        Invoker closeScene=new Invoker(close);
        closeScene.execute("", event);
    }
}
