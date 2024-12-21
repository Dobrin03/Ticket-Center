module org.example.ticketcenter {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires com.oracle.database.jdbc;
    requires log4j;

    exports org.example.ticketcenter.controllers;
    opens org.example.ticketcenter.controllers to javafx.fxml;
    exports org.example.ticketcenter.application;
    opens org.example.ticketcenter.application to javafx.fxml;

    opens org.example.ticketcenter.user_factory.models to javafx.base;
    exports org.example.ticketcenter.controllers.admin_controllers;
    opens org.example.ticketcenter.controllers.admin_controllers to javafx.fxml;
    opens org.example.ticketcenter.controllers.organisers_controllers to javafx.fxml;
    exports org.example.ticketcenter.controllers.organisers_controllers to javafx.fxml;
    opens org.example.ticketcenter.seats_data to javafx.base;
    exports org.example.ticketcenter.controllers.distributors_controllers to javafx.fxml;
    opens org.example.ticketcenter.controllers.distributors_controllers to javafx.fxml;
    opens org.example.ticketcenter.event_organiser_data to javafx.base;
    exports org.example.ticketcenter.controllers.client_controllers to javafx.fxml;
    opens org.example.ticketcenter.controllers.client_controllers to javafx.fxml;
    opens org.example.ticketcenter.event_data to javafx.base;
}