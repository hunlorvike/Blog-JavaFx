module hung.pj.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;
    requires jbcrypt;
    requires java.desktop;
    requires CustomStage;
    requires javax.mail.api;

    // Mở các gói bạn cần sử dụng trong JavaFX và jBCrypt
    opens hung.pj.login.model to javafx.base;
    opens hung.pj.login to javafx.fxml;
    opens hung.pj.login.controller to javafx.fxml;

    // Xuất các gói mà bạn muốn sử dụng từ bên ngoài module
    exports hung.pj.login;
    exports hung.pj.login.controller;

    // Cấu hình module cho JavaFX
    // Thay vì 'javafx.base', bạn cần chỉ định tên module JavaFX cụ thể
    requires javafx.base;
    requires javafx.graphics;
    requires org.glassfish.tyrus.server;
    requires org.glassfish.tyrus.client;
    requires datetime.picker.javafx;
    requires org.controlsfx.controls;

}
