package org.example.ticketcenter.controllers.distributors_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.event_data.EventData;
import org.example.ticketcenter.event_organiser_data.EventOrganiserData;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckRequestsController {
    @FXML
    private TableView<EventOrganiserData> ed_view;
    @FXML
    private TableColumn<EventOrganiserData,String> col_organiser;
    @FXML
    private TableColumn<EventOrganiserData,String> col_event;
    @FXML
    private TableColumn<EventOrganiserData, HBox> col_accept;
    private ObservableList<EventOrganiserData> data = FXCollections.observableArrayList();
    private DBConnection connection;
    private Distributor distributor;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        UserFactory userFactory=UserFactory.getInstance();
        distributor= (Distributor) userFactory.getUser();

        connection=DBConnection.getInstance();
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("call find_requests(?,?)");
        CallableStatement eventstmt=connection.getConnection().prepareCall("call find_event_by_id(?,?)");
        CallableStatement organiserstmt=connection.getConnection().prepareCall("call find_organiser_by_id(?,?)");
        EventData eventData = null;
        Organiser organiser = null;
        stmt.setInt(1,distributor.getID());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();
        ResultSet eventResult,organiserResult;
        eventstmt.registerOutParameter(2,OracleTypes.CURSOR);
        organiserstmt.registerOutParameter(2,OracleTypes.CURSOR);
        ResultSet resultSet= (ResultSet) stmt.getObject(2);
        while (resultSet.next()) {
            eventstmt.setInt(1,resultSet.getInt("event_id"));
            eventstmt.execute();
            eventResult= (ResultSet) eventstmt.getObject(2);
            if (eventResult.next()){
                eventData =new EventData(eventResult.getInt("Event_Id"),
                        eventResult.getString("Event_Name"),
                        eventResult.getInt("Ticket_Limit_Per_Person"),
                        eventResult.getDate("Event_Date"),
                        eventResult.getString("Event_Address"),
                        eventResult.getString("City_Name"),
                        eventResult.getString("Event_Type_Name"),
                        eventResult.getString("Event_Status_Name"));
            }
            organiserstmt.setInt(1,resultSet.getInt("organiser_id"));
            organiserstmt.execute();
            organiserResult= (ResultSet) organiserstmt.getObject(2);

            if (organiserResult.next()){
                userFactory.setResult(organiserResult);
                organiser= (Organiser) userFactory.getUser();
            }
         data.add(new EventOrganiserData(eventData,organiser));
        }

        col_organiser.setCellValueFactory(new PropertyValueFactory<EventOrganiserData,String>("organiserName"));
        col_accept.setCellValueFactory(new PropertyValueFactory<EventOrganiserData,HBox>("pane"));
        col_event.setCellValueFactory(new PropertyValueFactory<EventOrganiserData,String>("eventName"));
        ed_view.setItems(data);

        for(EventOrganiserData ed: ed_view.getItems()){
            ed.getAccept().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        answer(1, ed.getEvent().getId());
                        ed_view.getItems().remove(ed);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            ed.getDecline().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        answer(0, ed.getEvent().getId());
                        ed_view.getItems().remove(ed);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        connection.closeConnection();
    }

    private void answer(int value, int id) throws SQLException, ClassNotFoundException {
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL ANSWER_REQUEST(?, ?, ?)");
        stmt.setInt(1, value);
        stmt.setInt(2, id);
        stmt.setInt(3, distributor.getID());
        stmt.execute();
    }

    @FXML
    protected void onCancelClick(ActionEvent event) throws IOException {
        SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
        CloseSceneCommand close=new CloseSceneCommand(sceneAction);
        Invoker closeScene=new Invoker(close);
        closeScene.execute("", event);
    }
}
