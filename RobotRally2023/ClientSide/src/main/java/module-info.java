module com.dtu.clientside {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.net.http;
    requires com.google.gson;
    requires shared;


    exports com.dtu.clientside.controller;
    exports com.dtu.clientside.view;
    exports com.dtu.clientside;

}