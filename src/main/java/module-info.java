module org.example.ticketcenter {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.ticketcenter to javafx.fxml;
    exports org.example.ticketcenter;
}