module hung.pj.login {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;
    requires jbcrypt;
    requires java.desktop;
    requires CustomStage;

    opens hung.pj.login.model to javafx.base;
    opens hung.pj.login to javafx.fxml;
    exports hung.pj.login;
    exports hung.pj.login.controller;
    opens hung.pj.login.controller to javafx.fxml;
}

