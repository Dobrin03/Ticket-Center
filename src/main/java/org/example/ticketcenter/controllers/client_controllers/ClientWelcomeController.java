package org.example.ticketcenter.controllers.client_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.common.Constants;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.event_data.EventData;
import org.example.ticketcenter.event_data.ReservedEvent;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.Client;
import org.example.ticketcenter.user_factory.models.LoggedClient;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientWelcomeController {
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private LoggedClient client;
    @FXML
    private Label lbl_welcome;
    @FXML
    private TableView<EventData> event_view;
    @FXML
    private TableColumn<EventData, String> col_name;
    @FXML
    private TableColumn<EventData, String> col_type;
    @FXML
    private TableColumn<EventData, String> col_city;
    @FXML
    private TableColumn<EventData, Date> col_date;
    @FXML
    private TableColumn<EventData, String> col_status;
    private ObservableList<EventData> events= FXCollections.observableArrayList();
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        DBConnection connection=DBConnection.getInstance();
        client= LoggedClient.getInstance();
        client.setClient((Client) UserFactory.getInstance().getUser());
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(client.getClient().getName()).append("!");
        lbl_welcome.setText(builder.toString());

        col_name.setCellValueFactory(new PropertyValueFactory<EventData, String>("name"));
        col_type.setCellValueFactory(new PropertyValueFactory<EventData, String>("type"));
        col_city.setCellValueFactory(new PropertyValueFactory<EventData, String>("city"));
        col_date.setCellValueFactory(new PropertyValueFactory<EventData, Date>("date"));
        col_status.setCellValueFactory(new PropertyValueFactory<EventData, String>("status"));

        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_ALL_EVENTS(?)");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(1);

        while (resultSet.next()){
            events.add(new EventData(resultSet.getInt("Event_Id"),
                    resultSet.getString("Event_Name"),
                    resultSet.getInt("Ticket_Limit_Per_Person"),
                    resultSet.getDate("Event_Date"),
                    resultSet.getString("Event_Address"),
                    resultSet.getString("City_Name"),
                    resultSet.getString("Event_Type_Name"),
                    resultSet.getString("Event_Status_Name")));
        }

        event_view.setItems(events);
        connection.closeConnection();
    }

    @FXML
    private void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.LOG_IN, event);
        closeScene.execute("", event);
    }

    @FXML
    private void onBuyTicketsClick(ActionEvent event) throws IOException {
        ReservedEvent reservedEvent=ReservedEvent.getInstance();
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Selection error");
        alert.setContentText("Please select an event to buy tickets form!");
        if(!event_view.getSelectionModel().isEmpty()) {
            reservedEvent.setEventData(event_view.getSelectionModel().getSelectedItem());
            changeScene.execute(Constants.VIEW.BUY_TICKETS, event);
        }
        else{
            alert.showAndWait();
        }
    }
}
