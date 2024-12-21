package org.example.ticketcenter.controllers.distributors_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;
import org.example.ticketcenter.common.Constants;
import org.example.ticketcenter.database.DBConnection;
import org.example.ticketcenter.scene_actions.actions.SceneActionsImplication;
import org.example.ticketcenter.scene_actions.commands.ChangeSceneCommand;
import org.example.ticketcenter.scene_actions.commands.CloseSceneCommand;
import org.example.ticketcenter.scene_actions.invoker.Invoker;
import org.example.ticketcenter.user_factory.factories.UserFactory;
import org.example.ticketcenter.user_factory.models.Distributor;
import org.example.ticketcenter.user_factory.models.LoggedDistributor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DistributorWelcomeController {

    private SceneActionsImplication sceneAction=SceneActionsImplication.getInstance();
    private ChangeSceneCommand change=new ChangeSceneCommand(sceneAction);
    private CloseSceneCommand close=new CloseSceneCommand(sceneAction);
    private Invoker changeScene=new Invoker(change);
    private Invoker closeScene=new Invoker(close);
    private LoggedDistributor distributor;
    private DBConnection connection;
    @FXML
    private Label lbl_welcome;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        distributor= LoggedDistributor.getInstance();
        distributor.setDistributor((Distributor) UserFactory.getInstance().getUser());
        StringBuilder builder=new StringBuilder();
        builder.append(lbl_welcome.getText()).append(" ").append(distributor.getDistributor().getName()).append("!");
        lbl_welcome.setText(builder.toString());

        connection=DBConnection.getInstance();
        connection.connect();

        CallableStatement stmt=connection.getConnection().prepareCall("CALL NO_BOUGHT_TICKETS_DISTR(? ,?)");
        stmt.setInt(1, distributor.getDistributor().getID());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet result= (ResultSet) stmt.getObject(2);

        if(result.isBeforeFirst()){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event with no reservations");
            alert.setHeaderText("There are events with no reservations:");

            while(result.next()){
                alert.setContentText(alert.getContentText()+", "+result.getString("Event_Name"));
            }

            alert.showAndWait();
        }
        connection.closeConnection();
    }

    @FXML
    private void onCheckRequestsClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.CHECK_REQUESTS, event);
    }
    @FXML
    private void onLogOutClick(ActionEvent event) throws IOException {
        changeScene.execute(Constants.VIEW.LOG_IN, event);
        closeScene.execute("", event);
    }

    @FXML
    private void onCheckRatingClick() throws IOException, SQLException, ClassNotFoundException {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Your rating");
        alert.setTitle("Rating");
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL CHECK_RATING(?, ?)");
        stmt.setInt(1, distributor.getDistributor().getID());
        stmt.registerOutParameter(2, Types.NUMERIC);
        stmt.execute();

        BigDecimal rating = (BigDecimal) stmt.getObject(2);

        if(rating!=null){
            alert.setContentText("Your current rating is: "+rating);
        }
        else{
            alert.setContentText("You haven't been rated yet");
        }
        alert.showAndWait();
        connection.closeConnection();
    }

    @FXML
    private void onBoughtTicketsClick() throws SQLException, ClassNotFoundException {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bought Tickets");
        alert.setHeaderText("Bought tickets distributed by you:");

        DBConnection connection=DBConnection.getInstance();
        connection.connect();
        CallableStatement stmt=connection.getConnection().prepareCall("CALL SOLD_TICKETS_DISTR(?, ?)");
        stmt.setInt(1, distributor.getDistributor().getID());
        stmt.registerOutParameter(2, OracleTypes.CURSOR);
        stmt.execute();

        ResultSet resultSet= (ResultSet) stmt.getObject(2);

        if(!resultSet.isBeforeFirst()){
            alert.setContentText("No purchases yet");
        }

        while (resultSet.next()){
            alert.setContentText(alert.getContentText()+"\n"+resultSet.getString("Event_Name")+" - "+resultSet.getString("tickets"));
        }

        alert.showAndWait();
        connection.closeConnection();
    }
}
