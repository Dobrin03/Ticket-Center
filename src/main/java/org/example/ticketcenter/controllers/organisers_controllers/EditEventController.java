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
import org.example.ticketcenter.user_factory.models.Distributor;
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
    private Event updateEvent;
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
            seatTypeNames.add(resultSet.getString("Seat_Type_Name"));
        }

        type=connection.getConnection().prepareCall("CALL FIND_ALL_DISTRIBUTORS(?)");

        type.registerOutParameter(1, OracleTypes.CURSOR);
        type.execute();

        resultSet= (ResultSet) type.getObject(1);

        while (resultSet.next()){
            distributors.add(new Distributor(resultSet.getInt("Distributor_ID"),
                    resultSet.getString("Distributor_Name"),
                    resultSet.getString("Distributor_User"),
                    resultSet.getString("Distributor_Pass"),
                    resultSet.getBigDecimal("Distributor_Fee")));
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
    private void updateCell(TableColumn.CellEditEvent<SeatsData, ?> event){
        SeatsData seatsData=event.getRowValue();

        switch (event.getTableColumn().getText()){
            case "Type":
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
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL ES_DEL(?, ?)");
        CallableStatement edStmt=connection.getConnection().prepareCall("CALL ED_DEL(?, ?, ?)");
        stmt.setInt(1, updateEvent.getId());
        edStmt.setInt(1, updateEvent.getId());
        TableView.TableViewSelectionModel<SeatsData> selected=seat_view.getSelectionModel();

        if(!selected.isEmpty()){
            ObservableList<Integer> list = selected.getSelectedIndices();
            Integer[] selectedIndices=new Integer[list.size()];
            selectedIndices=list.toArray(selectedIndices);

            Arrays.sort(selectedIndices);

            for(int i=selectedIndices.length-1; i>=0; i--){
                selected.clearSelection(selectedIndices[i].intValue());

                edStmt.setInt(2, seat_view.getItems().get(selectedIndices[i]).getID());
                for(Distributor distr : distributor_view.getItems()){
                    edStmt.setInt(3, distr.getID());
                    edStmt.execute();
                }

                stmt.setInt(2,seat_view.getItems().get(selectedIndices[i]).getID());
                stmt.execute();
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
                int cityID = 0, typeID = 0, seatID = 0, statusId = 0, esID=0;
                CallableStatement ins=connection.getConnection().prepareCall("CALL CITY_INS(?)");
                CallableStatement check=connection.getConnection().prepareCall("CALL CHECK_CITY(?, ?)");
                CallableStatement edIns=connection.getConnection().prepareCall("CALL ED_DEL(?,?,?)");
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
                ins.setInt(1, updateEvent.getId());
                ins.setString(2, name_field.getText());
                ins.setInt(3, Integer.parseInt(limit_field.getText()));
                if(date_field.getEditor().getText().isEmpty()) {
                    ins.setDate(4, null);
                }
                else {
                    ins.setDate(4, Date.valueOf(date_field.getEditor().getText()));
                }
                ins.setString(5, address_field.getText());
                ins.setInt(6, cityID);
                ins.setInt(7, typeID);
                ins.setInt(8, statusId);
                ins.execute();



                ins=connection.getConnection().prepareCall("CALL ES_DEL(?, ?)");
                ins.setInt(1, updateEvent.getId());
                edIns.setInt(1, updateEvent.getId());

                for(SeatsData seatsData : seatTypeData){
                    check=connection.getConnection().prepareCall("CALL CHECK_STYPE(?, ?)");
                    check.setString(1, seatsData.getType());
                    check.registerOutParameter(2, OracleTypes.CURSOR);
                    check.execute();

                    result= (ResultSet) check.getObject(2);

                    while (result.next()){
                        seatID= result.getInt("Seat_Type_ID");
                    }

                    edIns.setInt(2, seatID);
                    for(Distributor distr : distributor_view.getItems()){
                        edIns.setInt(3, distr.getID());
                        edIns.execute();
                    }

                    ins.setInt(2, seatID);
                    ins.execute();
                }

                ins=connection.getConnection().prepareCall("CALL ES_INS(?, ?, ?, ?, ?)");
                ins.setInt(1, updateEvent.getId());

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
                    ins.registerOutParameter(5, Types.INTEGER);
                    ins.execute();

                    esID=ins.getInt(5);

                    edIns=connection.getConnection().prepareCall("CALL ED_INS(?, ?)");
                    edIns.setInt(1, esID);

                    for(Distributor dist: addedDistr){
                        edIns.setInt(2, dist.getID());
                        edIns.execute();
                    }
                }

                for(Distributor distr: distributor_view.getItems()){
                    distr.getAdd().setSelected(false);
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

                event_cb.getItems().remove(updateEvent);

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

        for(Event event:events){
            if(index==events.indexOf(event)){
                updateEvent=event;
            }
        }

        name_field.setText(updateEvent.getName());
        address_field.setText(updateEvent.getAddress());
        city_field.setText(updateEvent.getCity());
        limit_field.setText(String.valueOf(updateEvent.getLimit()));
        type_field.setText(updateEvent.getType());
        status_cb.getSelectionModel().select(updateEvent.getStatus());
        if(updateEvent.getDate()==null) {
            date_field.getEditor().setText("");
        }else{
            date_field.getEditor().setText(String.valueOf(updateEvent.getDate()));
        }

        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_EVENT_SEATS(?, ?)");
        stmt.setInt(1, updateEvent.getId());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(2);

        while (resultSet.next()){
            seatTypeData.add(new SeatsData(resultSet.getInt("SEAT_TYPE_ID"),
                    resultSet.getString("SEAT_TYPE_NAME"),
                    resultSet.getInt("SEAT_QUANTITY"),
                    resultSet.getBigDecimal("SEAT_PRICE")));
        }

        seat_view.setItems(seatTypeData);
        connection.closeConnection();
    }
}