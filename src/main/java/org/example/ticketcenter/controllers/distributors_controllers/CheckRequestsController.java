package org.example.ticketcenter.controllers.distributors_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.event_data.Event;
import org.example.ticketcenter.event_distributor_data.EventDistributorData;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckRequestsController {

    @FXML
    private TableView<EventDistributorData> ed_view;

    @FXML
    private TableColumn<EventDistributorData,String> col_organiser;

    @FXML
    private TableColumn<EventDistributorData,String> col_event;

    @FXML
    private TableColumn<EventDistributorData, HBox> col_accept;

    private ObservableList<EventDistributorData> data = FXCollections.observableArrayList();

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
        Event event = null;
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
                event=new Event(eventResult.getInt("Event_Id"),
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
         data.add(new EventDistributorData(event,organiser));
        }

        col_organiser.setCellValueFactory(new PropertyValueFactory<EventDistributorData,String>("organiserName"));
        col_accept.setCellValueFactory(new PropertyValueFactory<EventDistributorData,HBox>("pane"));
        col_event.setCellValueFactory(new PropertyValueFactory<EventDistributorData,String>("eventName"));
        ed_view.setItems(data);
    }
}
