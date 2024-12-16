module org.example.practica4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    requires org.controlsfx.controls;
    opens org.example.practica4.Models to javafx.base;

    opens org.example.practica4 to javafx.fxml;
    exports org.example.practica4;
    exports org.example.practica4.Controllers;
    opens org.example.practica4.Controllers to javafx.fxml;
}