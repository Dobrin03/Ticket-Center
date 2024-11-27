module org.example.ticketcenter {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires com.oracle.database.jdbc;

    exports org.example.ticketcenter.controllers;
    opens org.example.ticketcenter.controllers to javafx.fxml;
    exports org.example.ticketcenter.application;
    opens org.example.ticketcenter.application to javafx.fxml;

    opens org.example.ticketcenter.user_factory.models to javafx.base;
    exports org.example.ticketcenter.controllers.admin_controllers;
    opens org.example.ticketcenter.controllers.admin_controllers to javafx.fxml;
    opens org.example.ticketcenter.controllers.organisers_controllers to javafx.fxml;
    exports org.example.ticketcenter.controllers.organisers_controllers to javafx.fxml;
}