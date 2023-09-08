package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private Label alertLabel;
    @FXML
    private TextField nameTextField, emailTextField, roleTextField;
    @FXML
    private PasswordField currentPassTextField, newPassTextField, reNewPassTextField;
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser; // Đặt biến loggedInUser ở mức lớp để nó có thể được truy cập từ các phương thức
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        loggedInUser = userSingleton.getLoggedInUser();

        // Đặt giá trị của các TextField dựa trên thông tin người dùng
        nameTextField.setText(loggedInUser.getFullname());
        emailTextField.setText(loggedInUser.getEmail());
        roleTextField.setText(loggedInUser.getRole());
    }


    public void handleClickSave(ActionEvent event) {
        loggedInUser = userSingleton.getLoggedInUser();

        String currentPassword = currentPassTextField.getText().trim();
        String newPassword = newPassTextField.getText().trim();
        String renewPassword = reNewPassTextField.getText().trim();
        if (newPassword.equals(renewPassword)) {
            userDao.changeUserPassword(loggedInUser.getEmail().trim(), currentPassword, newPassword);
            alertLabel.setText("Đổi mật khẩu thành công");
        } else {
            showAlert("Mật khẩu không giống nhau");
        }

    }

    private void showAlert(String message) {
        alertLabel.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), timelineEvent -> {
                    alertLabel.setText(null);
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

}
