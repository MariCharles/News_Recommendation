module com.example.cw_2601 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires java.sql;
    requires org.jsoup;
    requires librec.core;
    requires java.desktop;

    opens com.example.cw_2601 to javafx.fxml;
    exports com.example.cw_2601;
    exports Models;
    opens Models to javafx.fxml;
    exports Services;
    opens Services to javafx.fxml;
}