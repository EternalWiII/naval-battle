module com.navel.navalbattle {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.navel.navalbattle to javafx.fxml;
    exports com.navel.navalbattle;
}