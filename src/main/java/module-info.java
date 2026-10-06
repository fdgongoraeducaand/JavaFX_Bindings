module com.example.javafx_bindings {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafx_bindings to javafx.fxml;
    exports com.example.javafx_bindings;
}