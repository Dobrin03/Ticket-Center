package org.example.ticketcenter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.models.Admin;
import org.example.ticketcenter.user_factory.models.Client;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class EditUserController {
    @FXML
    private TableView<User> user_view;

    @FXML
    private TableColumn<User, Integer> col_id;

    @FXML
    private TableColumn<User, String> col_name;

    @FXML
    private TableColumn<User, String> col_user;

    @FXML
    private TableColumn<User, String> col_pass;

    @FXML
    private TableColumn<User, BigDecimal> col_fee;

    @FXML
    private TableColumn<User, BigDecimal> col_rating;

    @FXML
    private TableColumn<User, String> col_email;

    @FXML
    private TableColumn<User, String> col_number;

    @FXML
    private TableColumn<User, String> col_city;

    @FXML
    private TableColumn<User, String> col_address;
    private String update, delete, query;
    private DBConnection connection;
    private ObservableList<User>users = FXCollections.observableArrayList();

    private UserFactory userFactory=UserFactory.getInstance();

    @FXML
    public void initialize(){
        user_view.setEditable(true);
        col_id.setEditable(false);

        col_id.setVisible(false);
        col_name.setVisible(false);
        col_user.setVisible(false);
        col_pass.setVisible(false);
        col_fee.setVisible(false);
        col_rating.setVisible(false);
        col_email.setVisible(false);
        col_number.setVisible(false);
        col_address.setVisible(false);
        col_city.setVisible(false);

        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("ID"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col_user.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        col_pass.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        col_fee.setCellValueFactory(new PropertyValueFactory<User, BigDecimal>("fee"));
        col_rating.setCellValueFactory(new PropertyValueFactory<User, BigDecimal>("rating"));
        col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        col_address.setCellValueFactory(new PropertyValueFactory<User, String>("address"));
        col_number.setCellValueFactory(new PropertyValueFactory<User, String>("number"));
        col_city.setCellValueFactory(new PropertyValueFactory<User, String>("city"));

        col_name.setCellFactory(TextFieldTableCell.forTableColumn());
        col_user.setCellFactory(TextFieldTableCell.forTableColumn());
        col_pass.setCellFactory(TextFieldTableCell.forTableColumn());
        col_fee.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        col_rating.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        col_email.setCellFactory(TextFieldTableCell.forTableColumn());
        col_address.setCellFactory(TextFieldTableCell.forTableColumn());
        col_number.setCellFactory(TextFieldTableCell.forTableColumn());
        col_city.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @FXML
    public void onOrganiserDataClick() throws SQLException, ClassNotFoundException {
        col_id.setVisible(true);
        col_name.setVisible(true);
        col_user.setVisible(true);
        col_pass.setVisible(true);
        col_fee.setVisible(false);
        col_rating.setVisible(false);
        col_email.setVisible(false);
        col_number.setVisible(false);
        col_address.setVisible(false);
        col_city.setVisible(false);

        query="SELECT * FROM ORGANISER_DATA";
        update="CALL ORGANISER_UPD(?, ?, ?, ?)";
        delete="CALL ORGANISER_DEL(?)";

        loadTable(query);
    }

    @FXML
    public void onDistributorDataClick() throws SQLException, ClassNotFoundException {
        col_id.setVisible(true);
        col_name.setVisible(true);
        col_user.setVisible(true);
        col_pass.setVisible(true);
        col_fee.setVisible(true);
        col_rating.setVisible(true);
        col_email.setVisible(false);
        col_number.setVisible(false);
        col_address.setVisible(false);
        col_city.setVisible(false);

        query="SELECT * FROM DISTRIBUTOR_DATA";
        update="CALL DISTRIBUTOR_UPD(?, ?, ?, ?, ?, ?)";
        delete="CALL DISTRIBUTOR_DEL(?)";

        loadTable(query);
    }

    @FXML
    public void onClientDataClick() throws SQLException, ClassNotFoundException {
        col_id.setVisible(true);
        col_name.setVisible(true);
        col_user.setVisible(true);
        col_pass.setVisible(true);
        col_email.setVisible(true);
        col_fee.setVisible(false);
        col_rating.setVisible(false);
        col_number.setVisible(true);
        col_address.setVisible(true);
        col_city.setVisible(true);

        query="SELECT c.client_id, c.client_name, c.client_user, c.client_pass, " +
                "c.client_email, c.client_number, ct.city_name, c.client_address " +
                "FROM CLIENT_DATA c JOIN CITY ct ON c.CITY_ID = ct.CITY_ID";
        update="CALL CLIENT_UPD(?, ?, ?, ?, ?, ?, ?, ?)";
        delete="CALL CLIENT_DEL(?)";

        loadTable(query);
    }

    private void loadTable(String query) throws SQLException, ClassNotFoundException {
        connection=DBConnection.getInstance();
        connection.connect();
        users.clear();
        Statement statement=connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result=statement.executeQuery(query);
        while(true){
            userFactory.setResult(result);
            users.add(userFactory.getUser());

            if(result.isLast()){
                break;
            }
        }

        user_view.setItems(users);
        connection.closeConnection();
    }

    @FXML
    private void updateCell(TableColumn.CellEditEvent<User, ?> event) throws SQLException, ClassNotFoundException {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        connection=DBConnection.getInstance();
        connection.connect();
        String cityQuery="SELECT City_ID FROM City WHERE City_Name = ?";
        String cityIns="CALL CITY_INS(?)";
        PreparedStatement checkCity=connection.getConnection().prepareStatement(cityQuery);
        PreparedStatement cityInsStmt=connection.getConnection().prepareStatement(cityIns);
        PreparedStatement statement=connection.getConnection().prepareStatement(update);


        if(update.contains("ORGANISER")){
            Organiser organiser= (Organiser) event.getRowValue();

            switch (event.getTableColumn().getText()){
                case "Name":
                    organiser.setName(event.getNewValue().toString());
                    break;
                case "Username":
                    organiser.setUsername(event.getNewValue().toString());
                    break;
                case "Password":
                    organiser.setPassword(event.getNewValue().toString());
                    break;
            }

            statement.setString(1, organiser.getName());
            statement.setInt(2, organiser.getID());
            statement.setString(3, organiser.getUsername());
            statement.setString(4, organiser.getPassword());
        } else if(update.contains("DISTRIBUTOR")){
            Distributor distributor= (Distributor) event.getRowValue();

            switch (event.getTableColumn().getText()){
                case "Name":
                    distributor.setName(event.getNewValue().toString());
                    break;
                case "Username":
                    distributor.setUsername(event.getNewValue().toString());
                    break;
                case "Password":
                    distributor.setPassword(event.getNewValue().toString());
                    break;
                case "Fee":
                    if(event.getNewValue().toString().matches("[0-9.]*")) {
                        distributor.setFee((BigDecimal) event.getNewValue());
                    }
                    else{
                        alert.setTitle("Fee Error");
                        alert.setContentText("Please insert a number");
                        alert.setHeaderText("Fee Error");
                        alert.showAndWait();
                    }
                    break;
                case "Rating":
                    if(event.getNewValue().toString().matches("[0-9.]*")) {
                        distributor.setRating((BigDecimal) event.getNewValue());
                    }
                    else{
                    alert.setTitle("Rating Error");
                    alert.setContentText("Please insert a number");
                    alert.setHeaderText("Rating Error");
                    alert.showAndWait();
                    }
                    break;
            }

            statement.setString(1, distributor.getName());
            statement.setInt(2, distributor.getID());
            statement.setString(3, distributor.getUsername());
            statement.setString(4, distributor.getPassword());
            statement.setBigDecimal(5, distributor.getFee());
            statement.setBigDecimal(6, distributor.getRating());
        } else if(update.contains("CLIENT")){
            Client client= (Client) event.getRowValue();
            int cityIndex = 0;

            switch (event.getTableColumn().getText()){
                case "Name":
                    client.setName(event.getNewValue().toString());
                    break;
                case "Username":
                    client.setUsername(event.getNewValue().toString());
                    break;
                case "Password":
                    client.setPassword(event.getNewValue().toString());
                    break;
                case "Number":
                    if(event.getNewValue().toString().length()==10 && event.getNewValue().toString().matches("[0-9]*")) {
                        client.setNumber(event.getNewValue().toString());
                    }
                    else{
                        alert.setTitle("Number Error");
                        alert.setContentText("Please insert a valid phone number");
                        alert.setHeaderText("Phone Number Error");
                        alert.showAndWait();
                    }
                    break;
                case "Address":
                    client.setAddress(event.getNewValue().toString());
                    break;
                case "Email":
                    client.setEmail(event.getNewValue().toString());
                    break;
                case "City":
                    client.setCity(event.getNewValue().toString());
                    break;
            }

            checkCity.setString(1, client.getCity());
            ResultSet result= checkCity.executeQuery();

            if(!result.isBeforeFirst()){
                cityInsStmt.setString(1, client.getCity());
                cityInsStmt.execute();
                result= checkCity.executeQuery();
            }

            while(result.next()){
                cityIndex=result.getInt("City_ID");
            }

            statement.setString(1, client.getName());
            statement.setInt(2, client.getID());
            statement.setString(3, client.getUsername());
            statement.setString(4, client.getPassword());
            statement.setString(5, client.getEmail());
            statement.setString(6, client.getAddress());
            statement.setString(7, client.getNumber());
            statement.setInt(8, cityIndex);
        }

        statement.execute();
        connection.closeConnection();
    }

    @FXML
    private void deleteRow() throws SQLException, ClassNotFoundException {
        Alert alert;
        TableView.TableViewSelectionModel<User> selected=user_view.getSelectionModel();

        if(!selected.isEmpty()){
            connection.connect();
            PreparedStatement del=connection.getConnection().prepareStatement(delete);
            del.setInt(1, selected.getSelectedItem().getID());
            ObservableList<Integer> list = selected.getSelectedIndices();
            Integer[] selectedIndices=new Integer[list.size()];
            selectedIndices=list.toArray(selectedIndices);

            Arrays.sort(selectedIndices);

            for(int i=selectedIndices.length-1; i>=0; i--){
                selected.clearSelection(selectedIndices[i].intValue());
                user_view.getItems().remove(selectedIndices[i].intValue());
                del.execute();
            }
            connection.closeConnection();

            alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("The item has been deleted successfully");
            alert.setHeaderText("Successful deletion");
            alert.showAndWait();
        }
        else {
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setContentText("No row selected to delete");
            alert.setHeaderText("Delete Error");
            alert.showAndWait();
        }
    }
}
