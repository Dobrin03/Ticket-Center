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
import org.example.ticketcenter.event_data.EventData;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.seats_data.SeatsData;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.LoggedOrganiser;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML
    private TableView<Distributor> distributor_view;
    @FXML
    private TableColumn<Distributor, String> col_dis_name;
    @FXML
    private TableColumn<Distributor, BigDecimal> col_dis_fee;
    @FXML
    private TableColumn<Distributor, CheckBox> col_dis_add;
    private ObservableList<Distributor> distributors = FXCollections.observableArrayList();
    private ObservableList<String> status = FXCollections.observableArrayList();
    private ObservableList<String> seatTypeNames = FXCollections.observableArrayList();
    private ObservableList<SeatsData> seatTypeData = FXCollections.observableArrayList();
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private ObservableList<EventData> eventData = FXCollections.observableArrayList();
    private Invoker closeScene=new Invoker(close);
    private LoggedOrganiser organiser;
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
    private EventData updateEventData;
    private List<Integer> seatIDs = new ArrayList<>();
    private List<Integer> distrIDs=new ArrayList<>();
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        organiser= LoggedOrganiser.getInstance();
        UserFactory userFactory=UserFactory.getInstance();
        connection=DBConnection.getInstance();
        connection.connect();
        CallableStatement type=connection.getConnection().prepareCall("CALL FIND_SEAT_TYPE(?)");
        type.registerOutParameter(1, OracleTypes.CURSOR);
        type.execute();

        ResultSet resultSet= (ResultSet) type.getObject(1);

        while (resultSet.next()){
            seatTypeNames.add(resultSet.getString("Seat_Type_Name"));
        }

        type=connection.getConnection().prepareCall("CALL FIND_ALL_DISTRIBUTORS(?)");

        type.registerOutParameter(1, OracleTypes.CURSOR);
        type.execute();

        resultSet= (ResultSet) type.getObject(1);

        while (resultSet.next()){
            userFactory.setResult(resultSet);
            distributors.add((Distributor) userFactory.getUser());
        }

        seat_view.setEditable(true);
        col_type.setCellValueFactory(new PropertyValueFactory<SeatsData, String>("type"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<SeatsData, Integer>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<SeatsData, BigDecimal>("price"));

        col_type.setCellFactory(ComboBoxTableCell.forTableColumn(seatTypeNames));
        col_quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col_price.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        col_dis_name.setCellValueFactory(new PropertyValueFactory<Distributor, String>("name"));
        col_dis_fee.setCellValueFactory(new PropertyValueFactory<Distributor, BigDecimal>("fee"));
        col_dis_add.setCellValueFactory(new PropertyValueFactory<Distributor, CheckBox>("add"));

        distributor_view.setItems(distributors);

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
        distributor_view.setDisable(true);

        connection.connect();

        type=connection.getConnection().prepareCall("call find_events(?,?)");
        type.setInt(1,organiser.getOrganiser().getID());
        type.registerOutParameter(2,OracleTypes.CURSOR);
        type.execute();
        resultSet= (ResultSet) type.getObject(2);
        while (resultSet.next()){
            eventData.add(new EventData(resultSet.getInt("Event_Id"),
                    resultSet.getString("Event_Name"),
                    resultSet.getInt("Ticket_Limit_Per_Person"),
                    resultSet.getDate("Event_Date"),
                    resultSet.getString("Event_Address"),
                    resultSet.getString("City_Name"),
                    resultSet.getString("Event_Type_Name"),
                    resultSet.getString("Event_Status_Name")));
        }
        for (EventData event : eventData) {
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
        seat_view.getItems().add(new SeatsData(0,null, 0 , null));
        List<String> values=new ArrayList<>();

        for(SeatsData seatsData : seat_view.getItems()){
            values.add(col_type.getCellData(seatsData));
        }

        Iterator<String> iterator = seatTypeNames.iterator();
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
    private void updateCell(TableColumn.CellEditEvent<SeatsData, ?> event) throws SQLException, ClassNotFoundException {
        SeatsData seatsData=event.getRowValue();

        switch (event.getTableColumn().getText()){
            case "Type":
                connection.connect();
                CallableStatement check=connection.getConnection().prepareCall("CALL CHECK_STYPE(?, ?)");
                check.setString(1, event.getNewValue().toString());
                check.registerOutParameter(2, OracleTypes.CURSOR);
                check.execute();

                ResultSet result= (ResultSet) check.getObject(2);

                while (result.next()){
                    seatsData.setID(result.getInt("Seat_Type_ID"));
                }
                connection.closeConnection();

                seatsData.setType(event.getNewValue().toString());
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
                selected.clearSelection(selectedIndices[i]);
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
        connection.closeConnection();
    }

    @FXML
    private void onUpdateClick() throws SQLException, ClassNotFoundException {
        List<Distributor> addedDistr=new ArrayList<>();

        for(Distributor distr: distributor_view.getItems()){
            if(distr.getAdd().isSelected()){
                addedDistr.add(distr);
            }
        }

        if(!name_field.getText().isEmpty() && !limit_field.getText().isEmpty() && !city_field.getText().isEmpty()
        && !address_field.getText().isEmpty() && !type_field.getText().isEmpty() && !seat_view.getItems().isEmpty()
        && !addedDistr.isEmpty()){
            if(limit_field.getText().matches("[0-9]*")){
                connection.connect();
                int cityID = 0, typeID = 0, seatID = 0, statusId = 0;
                List<Integer> edIDs = new ArrayList<>();
                CallableStatement ins=connection.getConnection().prepareCall("CALL CITY_INS(?)");
                CallableStatement check=connection.getConnection().prepareCall("CALL CHECK_CITY(?, ?)");
                CallableStatement del=connection.getConnection().prepareCall("CALL ES_DEL(?,?)");
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

                check=connection.getConnection().prepareCall("CALL CHECK_STATUS(?, ?)");
                check.setString(1, status_cb.getValue());
                check.registerOutParameter(2, OracleTypes.CURSOR);
                check.execute();

                result= (ResultSet) check.getObject(2);

                while(result.next()){
                    statusId=result.getInt("event_status_id");
                }

                ins=connection.getConnection().prepareCall("CALL EVENT_UPD(?, ?, ?, ?, ?, ?, ?, ?)");
                ins.setInt(1, updateEventData.getId());
                ins.setString(2, name_field.getText());
                ins.setInt(3, Integer.parseInt(limit_field.getText()));
                if(date_field.getEditor().getText().isEmpty()) {
                    ins.setDate(4, null);
                }
                else {
                    ins.setDate(4,  Date.valueOf(date_field.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                }
                ins.setString(5, address_field.getText());
                ins.setInt(6, cityID);
                ins.setInt(7, typeID);
                ins.setInt(8, statusId);
                ins.execute();

                del.setInt(1, updateEventData.getId());
                for (Integer id : seatIDs) {
                    del.setInt(2, id);
                    del.execute();
                }

                del=connection.getConnection().prepareCall("CALL ED_DEL(?, ?)");
                del.setInt(1, updateEventData.getId());

                for(Integer id:distrIDs){
                    del.setInt(2, id);
                    del.execute();
                }

                CallableStatement edIns=connection.getConnection().prepareCall("CALL ED_INS(?, ?, ?)");
                edIns.setInt(1, updateEventData.getId());

                for(Distributor dist: addedDistr){
                     edIns.setInt(2, dist.getID());
                     edIns.registerOutParameter(3, Types.INTEGER);
                     edIns.execute();

                     edIDs.add(edIns.getInt(3));
                }

                ins=connection.getConnection().prepareCall("CALL ES_INS(?, ?, ?, ?)");

                for(SeatsData seatsData : seat_view.getItems()){
                    ins.setInt(1, seatsData.getID());
                    ins.setInt(2, seatsData.getQuantity());
                    ins.setBigDecimal(3, seatsData.getPrice());
                    for (Integer id : edIDs) {
                        ins.setInt(4, id);
                        ins.execute();
                    }
                }

                for(Distributor distr: distributor_view.getItems()){
                    distr.getAdd().setSelected(false);
                }

                eventData.clear();
                event_cb.getItems().clear();

                check=connection.getConnection().prepareCall("call find_events(?,?)");
                check.setInt(1,organiser.getOrganiser().getID());
                check.registerOutParameter(2,OracleTypes.CURSOR);
                check.execute();
                result= (ResultSet) check.getObject(2);
                while (result.next()){
                    eventData.add(new EventData(result.getInt("Event_Id"),
                            result.getString("Event_Name"),
                            result.getInt("Ticket_Limit_Per_Person"),
                            result.getDate("Event_Date"),
                            result.getString("Event_Address"),
                            result.getString("City_Name"),
                            result.getString("Event_Type_Name"),
                            result.getString("Event_Status_Name")));
                }
                for (EventData event : eventData) {
                    event_cb.getItems().add(event.getName());
                }

                name_field.clear();
                limit_field.clear();
                city_field.clear();
                address_field.clear();
                type_field.clear();
                date_field.getEditor().clear();
                seat_view.getItems().clear();

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
                distributor_view.setDisable(true);

                event_cb.getItems().remove(updateEventData);

                lbl_error.setText("Event updated successfully");

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
    private void setData() throws SQLException, ClassNotFoundException {
        seatTypeData.clear();
        distrIDs.clear();

        for(Distributor d: distributors){
            d.getAdd().setSelected(false);
        }

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
        distributor_view.setDisable(false);

        int index=event_cb.getSelectionModel().getSelectedIndex();

        for(EventData eventData : this.eventData){
            if(index== this.eventData.indexOf(eventData)){
                updateEventData = eventData;
            }
        }

        name_field.setText(updateEventData.getName());
        address_field.setText(updateEventData.getAddress());
        city_field.setText(updateEventData.getCity());
        limit_field.setText(String.valueOf(updateEventData.getLimit()));
        type_field.setText(updateEventData.getType());
        status_cb.getSelectionModel().select(updateEventData.getStatus());
        if(updateEventData.getDate()==null) {
            date_field.getEditor().setText("");
        }else{
            date_field.setValue(updateEventData.getDate().toLocalDate());
        }

        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_EVENT_SEATS(?, ?)");
        stmt.setInt(1, updateEventData.getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(2);

        while (resultSet.next()){
            seatTypeData.add(new SeatsData(resultSet.getInt("SEAT_TYPE_ID"),
                    resultSet.getString("SEAT_TYPE_NAME"),
                    resultSet.getInt("SEAT_QUANTITY"),
                    resultSet.getBigDecimal("SEAT_PRICE")));
            seatIDs.add(resultSet.getInt("SEAT_TYPE_ID"));
        }

        seat_view.setItems(seatTypeData);

        stmt=connection.getConnection().prepareCall("CALL FIND_DISTRIBUTING_BY_EVENT(?, ?)");
        stmt.setInt(1, updateEventData.getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        resultSet= (ResultSet) stmt.getObject(2);

        while (resultSet.next()) {
            distrIDs.add(resultSet.getInt("Distributor_ID"));
        }

        for(Distributor d: distributors){
            if(distrIDs.contains(d.getID())){
                d.getAdd().setSelected(true);
            }
        }
        connection.closeConnection();
    }
}