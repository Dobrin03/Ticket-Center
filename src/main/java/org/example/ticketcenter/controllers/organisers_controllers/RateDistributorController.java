package org.example.ticketcenter.controllers.organisers_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import oracle.jdbc.internal.OracleTypes;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.interfaces.User;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.Organiser;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RateDistributorController {
    @FXML
    private TableView<Distributor> distributor_view;
    @FXML
    private TableColumn<Distributor, String> col_name;
    @FXML
    private TextArea review_area;
    @FXML
    private RadioButton radio_5, radio_4, radio_3, radio_2, radio_1;
    @FXML
    private Label lbl_error;

    private ObservableList<Distributor> distributors= FXCollections.observableArrayList();
    private Organiser organiser;
    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker closeScene=new Invoker(close);
    private DBConnection connection;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        organiser= (Organiser) UserFactory.getInstance().getUser();
        connection=DBConnection.getInstance();
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL FIND_UNRATED_DISTRIBUTORS(?, ?)");
        stmt.setInt(1, organiser.getID());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(2);

        while (resultSet.next()){
            distributors.add(new Distributor(resultSet.getInt("Distributor_ID"),
                    resultSet.getString("Distributor_Name"),
                    resultSet.getString("Distributor_User"),
                    resultSet.getString("Distributor_Pass"),
                    resultSet.getBigDecimal("Distributor_Fee")));
        }

        connection.closeConnection();

        col_name.setCellValueFactory(new PropertyValueFactory<Distributor, String>("name"));

        distributor_view.setItems(distributors);
    }

    @FXML
    protected void onCancelClick(ActionEvent event) throws IOException {
        closeScene.execute("", event);
    }

    @FXML
    protected void onRateClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL Rating_Ins(?, ?, ?, ?)");

        if(!distributor_view.getSelectionModel().isEmpty() &&
                (radio_1.isSelected() || radio_2.isSelected() || radio_3.isSelected() || radio_4.isSelected() || radio_5.isSelected())) {
            if (radio_1.isSelected()) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(1));
                radio_1.setSelected(false);
            } else if (radio_2.isSelected()) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(2));
                radio_2.setSelected(false);
            } else if (radio_3.isSelected()) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(3));
                radio_3.setSelected(false);
            } else if (radio_4.isSelected()) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(4));
                radio_4.setSelected(false);
            } else if (radio_5.isSelected()) {
                stmt.setBigDecimal(1, BigDecimal.valueOf(5));
                radio_5.setSelected(false);
            }

            stmt.setInt(2, organiser.getID());
            stmt.setInt(3, distributor_view.getSelectionModel().getSelectedItem().getID());
            stmt.setString(4, review_area.getText());

            stmt.execute();

            distributors.remove(distributor_view.getSelectionModel().getSelectedItem());
            review_area.clear();
            lbl_error.setText("Rated successfully!");
        }
        else{
            lbl_error.setText("Please select needed information (*)");
        }

    }
}
