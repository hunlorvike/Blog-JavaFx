package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
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


    public void handleClickSave(ActionEvent event) throws IOException {
        loggedInUser = userSingleton.getLoggedInUser();

        String currentPassword = currentPassTextField.getText().trim();
        String newPassword = newPassTextField.getText().trim();
        String renewPassword = reNewPassTextField.getText().trim();
        if (newPassword.equals(renewPassword)) {
            userDao.changeUserPassword(loggedInUser.getEmail().trim(), currentPassword, newPassword);
            ControllerUtils.showAlertDialog("Đổi mật khẩu thành công", Alert.AlertType.INFORMATION);
            AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Mật không không giống nhau", Alert.AlertType.ERROR);

        }

    }

}
