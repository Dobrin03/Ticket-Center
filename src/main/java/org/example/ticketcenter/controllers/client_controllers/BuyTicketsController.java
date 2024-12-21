package org.example.ticketcenter.controllers.client_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.event_data.ReservedEvent;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.seats_data.SeatsData;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.LoggedClient;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Button btn_buy;
    @FXML
    private Label lbl_error;
    private ObservableList<SeatsData> seatsData= FXCollections.observableArrayList();
    private ObservableList<Distributor> distributors= FXCollections.observableArrayList();
    private ReservedEvent reservedEvent;
    private DBConnection connection;
    private UserFactory userFactory;
    private LoggedClient client;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        client=LoggedClient.getInstance();
        reservedEvent= ReservedEvent.getInstance();
        userFactory=UserFactory.getInstance();
        Organiser organiser = null;
        lbl_name.setText(lbl_name.getText()+reservedEvent.getEventData().getName());
        lbl_type.setText(lbl_type.getText()+reservedEvent.getEventData().getType());
        lbl_city.setText(lbl_city.getText()+reservedEvent.getEventData().getCity());
        lbl_date.setText(lbl_date.getText()+reservedEvent.getEventData().getDate());
        lbl_limit.setText(lbl_limit.getText()+reservedEvent.getEventData().getLimit());
        lbl_status.setText(lbl_status.getText()+reservedEvent.getEventData().getStatus());

        connection=DBConnection.getInstance();
        connection.connect();

        CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_ORGANISER_BY_EVENT(?, ?)");
        stmt.setInt(1, reservedEvent.getEventData().getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(2);

        while(resultSet.next()){
            userFactory.setResult(resultSet);
            organiser= (Organiser) userFactory.getUser();
        }

        connection.connect();

        lbl_organiser.setText(lbl_organiser.getText()+organiser.getName());

        stmt=connection.getConnection().prepareCall("CALL FIND_EVENT_SEATS(?, ?)");
        stmt.setInt(1, reservedEvent.getEventData().getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        resultSet= (ResultSet) stmt.getObject(2);

        while (resultSet.next()){
            seatsData.add(new SeatsData(resultSet.getInt("SEAT_TYPE_ID"),
                    resultSet.getString("SEAT_TYPE_NAME"),
                    resultSet.getInt("SEAT_QUANTITY"),
                    resultSet.getBigDecimal("SEAT_PRICE")));
        }

        connection.connect();

        stmt=connection.getConnection().prepareCall("CALL FIND_DISTRIBUTING_BY_EVENT(?, ?)");
        stmt.setInt(1, reservedEvent.getEventData().getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        resultSet= (ResultSet) stmt.getObject(2);

        while(resultSet.next()){
            userFactory.setResult(resultSet);
            distributors.add((Distributor) userFactory.getUser());
        }

        for(Distributor d: distributors){
            cb_distributors.getItems().add(d.getName());
        }

        col_type.setCellValueFactory(new PropertyValueFactory<SeatsData, String>("type"));
        col_available.setCellValueFactory(new PropertyValueFactory<SeatsData, Integer>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<SeatsData, BigDecimal>("price"));
        col_reserve.setCellValueFactory(new PropertyValueFactory<SeatsData, Integer>("boughtSeats"));

        seat_view.setEditable(true);
        col_type.setEditable(false);
        col_available.setEditable(false);
        col_price.setEditable(false);
        col_reserve.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        seat_view.setItems(seatsData);
        if(lbl_status.getText().contains("To be determined")){
            seat_view.setDisable(true);
            cb_distributors.setDisable(true);
            btn_buy.setDisable(true);
        }
        connection.closeConnection();
    }

    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
        CloseSceneCommand close=new CloseSceneCommand(sceneAction);
        Invoker closeScene=new Invoker(close);
        closeScene.execute("", event);
    }

    @FXML
    private void updateReserve(TableColumn.CellEditEvent<SeatsData, ?> event){
        SeatsData seatData=event.getRowValue();
        seatData.setBoughtSeats(Integer.parseInt(event.getNewValue().toString()));
    }

    @FXML
    private void onBuyClick() throws SQLException, ClassNotFoundException{
        int reservedSeats=0;
        for(SeatsData s:seat_view.getItems()){
            reservedSeats+=s.getBoughtSeats();
        }

        if(reservedSeats>0 && !cb_distributors.getSelectionModel().isEmpty()){
            int distrID = 0;
            HashMap<Integer, Integer> bought_event_seats=new HashMap<>();

            for(Distributor d: distributors){
                if(d.getName().equals(cb_distributors.getSelectionModel().getSelectedItem())){
                    distrID=d.getID();
                }
            }

            connection.connect();
            CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_EVENT_SEATS_ID(?, ?, ?, ?)");
            stmt.setInt(1, reservedEvent.getEventData().getId());
            stmt.setInt(2, distrID);
            stmt.registerOutParameter(4, Types.INTEGER);

            for(SeatsData s:seat_view.getItems()){
                if(s.getBoughtSeats()>0) {
                    stmt.setInt(3, s.getID());
                    stmt.execute();

                    bought_event_seats.put(stmt.getInt(4), s.getBoughtSeats());
                }
            }

            stmt=connection.getConnection().prepareCall("CALL TICKET_INS(?, ?, ?)");
            stmt.setInt(1, client.getClient().getID());

            for(Integer id: bought_event_seats.keySet()){
                stmt.setInt(2, id);
                stmt.setInt(3, bought_event_seats.get(id));

                try {
                    stmt.execute();
                }catch (SQLException e){
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Insert Error");
                    alert.setHeaderText("Limit exceeded");
                    alert.setContentText(e.getMessage().substring(e.getMessage().indexOf(":")+1, e.getMessage().indexOf("!")).trim());
                    alert.showAndWait();
                }

            }

            seatsData.clear();
            seat_view.getItems().clear();

            stmt=connection.getConnection().prepareCall("CALL FIND_EVENT_SEATS(?, ?)");
            stmt.setInt(1, reservedEvent.getEventData().getId());
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet resultSet= (ResultSet) stmt.getObject(2);

            while (resultSet.next()){
                seatsData.add(new SeatsData(resultSet.getInt("SEAT_TYPE_ID"),
                        resultSet.getString("SEAT_TYPE_NAME"),
                        resultSet.getInt("SEAT_QUANTITY"),
                        resultSet.getBigDecimal("SEAT_PRICE")));
            }

            seat_view.setItems(seatsData);


            lbl_error.setText("Purchase made successfully");
            connection.connect();
        }
        else{
            lbl_error.setText("Please fill up the needed data");
        }
    }
}
