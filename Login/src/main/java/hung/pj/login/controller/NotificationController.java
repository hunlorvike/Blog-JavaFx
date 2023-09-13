package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.Notifications.INotifications;
import hung.pj.login.dao.Notifications.NotificationsImpl;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.model.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class NotificationController {
    ConnectionProvider connectionProvider = new ConnectionProvider();
    NotificationsImpl notification = new NotificationsImpl(connectionProvider.getConnection());
    @FXML
    private VBox labelContainer;



    public NotificationController() {
    }

    public void initialize() {
//demo
        List<Notification> notificationData = notification.getAll();
        String name = notification.GetNameOfUser(1);
        // Tạo và thêm Lable
        for (Notification noti : notificationData) {
            Label label = new Label(name + noti.content);
            label.getStyleClass().add("notification-label"); // Thêm css
            labelContainer.getChildren().add(label);
        }
    }
}
