module com.example.cw_2601 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cw_2601 to javafx.fxml;
    exports com.example.cw_2601;
}