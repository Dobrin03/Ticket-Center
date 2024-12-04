package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.event_data.Event;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.seats_data.SeatsData;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EditEventController {
    @FXML
    private TableView<SeatsData> seat_view;
    @FXML
    private TableColumn<SeatsData, String> col_type;
    @FXML
    private TableColumn<SeatsData, Integer> col_quantity;
    @FXML
    private TableColumn<SeatsData, BigDecimal> col_price;
    private ObservableList<String> status = FXCollections.observableArrayList();
    private ObservableList<String> seatType = FXCollections.observableArrayList();
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private ObservableList<Event> events = FXCollections.observableArrayList();
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private Organiser organiser;
    private DBConnection connection;

    @FXML
    private TextField name_field, limit_field, city_field, address_field, type_field;

    @FXML
    private DatePicker date_field;

    @FXML
    private Label lbl_error;
    @FXML
    private ComboBox<String> event_cb;

    @FXML
    private ComboBox<String> status_cb;

    @FXML
    private Button add_seat_btn, update_btn, delete_seat_btn;
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        organiser= (Organiser) UserFactory.getInstance().getUser();
        connection=DBConnection.getInstance();
        connection.connect();
        String findTypes="CALL FIND_SEAT_TYPE(?)";
        CallableStatement type=connection.getConnection().prepareCall(findTypes);
        type.registerOutParameter(1, OracleTypes.CURSOR);
        type.execute();

        ResultSet resultSet= (ResultSet) type.getObject(1);

        while (resultSet.next()){
            seatType.add(resultSet.getString("Seat_Type_Name"));
        }


        seat_view.setEditable(true);
        col_type.setCellValueFactory(new PropertyValueFactory<SeatsData, String>("type"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<SeatsData, Integer>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<SeatsData, BigDecimal>("price"));

        col_type.setCellFactory(ComboBoxTableCell.forTableColumn(seatType));
        col_quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col_price.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        name_field.setDisable(true);
        address_field.setDisable(true);
        city_field.setDisable(true);
        date_field.setDisable(true);
        limit_field.setDisable(true);
        type_field.setDisable(true);

        status_cb.setDisable(true);
        add_seat_btn.setDisable(true);
        delete_seat_btn.setDisable(true);
        update_btn.setDisable(true);
        seat_view.setDisable(true);

        type=connection.getConnection().prepareCall("call find_events(?,?)");
        type.setInt(1,organiser.getID());
        type.registerOutParameter(2,OracleTypes.CURSOR);
        type.execute();
        resultSet= (ResultSet) type.getObject(2);
        while (resultSet.next()){
            events.add(new Event(resultSet.getInt("Event_Id"),
                    resultSet.getString("Event_Name"),
                    resultSet.getInt("Ticket_Limit_Per_Person"),
                    resultSet.getDate("Event_Date"),
                    resultSet.getString("Event_Address"),
                    resultSet.getString("City_Name"),
                    resultSet.getString("Event_Type_Name"),
                    resultSet.getString("Event_Status_Name")));
        }
        for (Event event:events) {
            event_cb.getItems().add(event.getName());
        }
        type=connection.getConnection().prepareCall("call find_STATUS(?)");
        type.registerOutParameter(1,OracleTypes.CURSOR);
        type.execute();
        resultSet= (ResultSet) type.getObject(1);
        while (resultSet.next()){
            status.add (resultSet.getString("event_status_name"));
        }
        for (String st:status) {
            status_cb.getItems().add(st);
        }
        connection.closeConnection();

    }
    @FXML
    protected void onCancelClick(ActionEvent event) throws IOException {
        closeScene.execute("", event);
    }

    @FXML
    protected void onAddSeatsClick(ActionEvent event) throws IOException {
        seat_view.getItems().add(new SeatsData(null, 0 , null));
        List<String> values=new ArrayList<>();

        for(SeatsData seatsData : seat_view.getItems()){
            values.add(col_type.getCellData(seatsData));
        }

        Iterator<String> iterator = seatType.iterator();
        while (iterator.hasNext()) {
            String type = iterator.next();
            for (String usedType : values) {
                if (type.equals(usedType)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @FXML
    private void updateCell(TableColumn.CellEditEvent<SeatsData, ?> event){
        SeatsData seatsData=event.getRowValue();

        switch (event.getTableColumn().getText()){
            case "Type":
                seatsData.setName(event.getNewValue().toString());
                break;
            case "Quantity":
                seatsData.setQuantity((int) event.getNewValue());
                break;
            case "Price":
                seatsData.setPrice((BigDecimal) event.getNewValue());
                break;
        }
    }

    @FXML
    private void deleteRow() throws SQLException, ClassNotFoundException {
        Alert alert;
        TableView.TableViewSelectionModel<SeatsData> selected=seat_view.getSelectionModel();

        if(!selected.isEmpty()){
            ObservableList<Integer> list = selected.getSelectedIndices();
            Integer[] selectedIndices=new Integer[list.size()];
            selectedIndices=list.toArray(selectedIndices);

            Arrays.sort(selectedIndices);

            for(int i=selectedIndices.length-1; i>=0; i--){
                selected.clearSelection(selectedIndices[i].intValue());
                seat_view.getItems().remove(selectedIndices[i].intValue());
            }
        }
        else {
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setContentText("No row selected to delete");
            alert.setHeaderText("Delete Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void onAddClick() throws SQLException, ClassNotFoundException {
        if(!name_field.getText().isEmpty() && !limit_field.getText().isEmpty() && !city_field.getText().isEmpty()
        && !address_field.getText().isEmpty() && !type_field.getText().isEmpty() && !seat_view.getItems().isEmpty()){
            if(limit_field.getText().matches("[0-9]*")){
                connection.connect();
                int cityID = 0, typeID = 0, eventID = 0, seatID = 0;
                CallableStatement ins=connection.getConnection().prepareCall("CALL CITY_INS(?)");
                CallableStatement check=connection.getConnection().prepareCall("CALL CHECK_CITY(?, ?)");
                check.setString(1, city_field.getText());
                check.registerOutParameter(2, OracleTypes.CURSOR);
                check.execute();

                ResultSet result= (ResultSet) check.getObject(2);

                if(!result.isBeforeFirst()){
                    ins.setString(1, city_field.getText());
                    ins.execute();
                    check.execute();
                    result= (ResultSet) check.getObject(2);
                }

                while(result.next()){
                    cityID=result.getInt("City_ID");
                }

                ins=connection.getConnection().prepareCall("CALL ETYPE_INS(?)");
                check=connection.getConnection().prepareCall("CALL CHECK_ETYPE(?, ?)");
                check.setString(1, type_field.getText());
                check.registerOutParameter(2, OracleTypes.CURSOR);
                check.execute();

                result= (ResultSet) check.getObject(2);

                if(!result.isBeforeFirst()){
                    ins.setString(1, type_field.getText());
                    ins.execute();
                    check.execute();
                    result= (ResultSet) check.getObject(2);
                }

                while(result.next()){
                    typeID=result.getInt("Event_Type_ID");
                }

                ins=connection.getConnection().prepareCall("CALL EVENT_INS(?, ?, ?, ?, ?, ?, ?, ?)");
                ins.setString(1, name_field.getText());
                ins.setInt(2, Integer.parseInt(limit_field.getText()));
                if(date_field.getEditor().getText().isEmpty()) {
                    ins.setDate(3, null);
                }
                else {
                    ins.setDate(3, Date.valueOf(date_field.getValue()));
                }
                ins.setString(4, address_field.getText());
                ins.setInt(5, cityID);
                ins.setInt(6, typeID);
                ins.setInt(7, organiser.getID());
                ins.registerOutParameter(8, Types.INTEGER);
                ins.execute();

                eventID=ins.getInt(8);

                ins=connection.getConnection().prepareCall("CALL ES_INS(?, ?, ?, ?)");
                ins.setInt(1, eventID);

                for(SeatsData seatsData : seat_view.getItems()){
                    check=connection.getConnection().prepareCall("CALL CHECK_STYPE(?, ?)");
                    check.setString(1, seatsData.getType());
                    check.registerOutParameter(2, OracleTypes.CURSOR);
                    check.execute();

                    result= (ResultSet) check.getObject(2);

                    while (result.next()){
                        seatID= result.getInt("Seat_Type_ID");
                    }

                    ins.setInt(2, seatID);
                    ins.setInt(3, seatsData.getQuantity());
                    ins.setBigDecimal(4, seatsData.getPrice());
                    ins.execute();
                }

                name_field.clear();
                limit_field.clear();
                city_field.clear();
                address_field.clear();
                type_field.clear();
                date_field.getEditor().clear();
                seat_view.getItems().clear();

                lbl_error.setText("Event created successfully");

                connection.closeConnection();
            }
            else {
                lbl_error.setText("Please input a number for the ticket limit");
            }
        }
        else {
            lbl_error.setText("Please fill up the necessary fields (*)");
        }
    }

    @FXML
    private void setData(){
        name_field.setDisable(false);
        address_field.setDisable(false);
        city_field.setDisable(false);
        date_field.setDisable(false);
        limit_field.setDisable(false);
        type_field.setDisable(false);

        status_cb.setDisable(false);
        add_seat_btn.setDisable(false);
        delete_seat_btn.setDisable(false);
        update_btn.setDisable(false);
        seat_view.setDisable(false);
    }
}