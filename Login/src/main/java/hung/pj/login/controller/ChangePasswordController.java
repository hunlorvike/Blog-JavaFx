package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import hung.pj.login.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    private Label alertLabel;
    @FXML
    private TextField nameTextField, emailTextField, roleTextField;
    @FXML
    private PasswordField currentPassTextField, newPassTextField, reNewPassTextField;

    private final UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;
    private final UserDaoImpl userDao;

    public ChangePasswordController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        userDao = new UserDaoImpl(connectionProvider.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInUser = userSingleton.getLoggedInUser();

        // Đặt giá trị của các TextField dựa trên thông tin người dùng
        nameTextField.setText(loggedInUser.getFullname());
        emailTextField.setText(loggedInUser.getEmail());
        roleTextField.setText(loggedInUser.getRole());
    }

    public void handleClickSave() throws IOException {
        loggedInUser = userSingleton.getLoggedInUser();

        String currentPassword = currentPassTextField.getText().trim();
        String newPassword = newPassTextField.getText().trim();
        String renewPassword = reNewPassTextField.getText().trim();

        if (newPassword.equals(renewPassword)) {
            if (!ValidationUtils.isValidPassword(newPassword)) {
                ControllerUtils.showAlertDialog("Password must be at least 8 characters.", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
                return;
            }

            boolean passwordChanged = userDao.changeUserPassword(loggedInUser.getEmail().trim(), currentPassword, newPassword);

            if (passwordChanged) {
                ControllerUtils.showAlertDialog("Đổi mật khẩu thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
            } else {
                ControllerUtils.showAlertDialog("Mật khẩu hiện tại không đúng", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }
        } else {
            ControllerUtils.showAlertDialog("Mật khẩu mới không giống nhau", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
        }
    }
}
