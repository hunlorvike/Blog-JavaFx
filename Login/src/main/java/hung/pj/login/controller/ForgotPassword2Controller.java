package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import hung.pj.login.ultis.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPassword2Controller implements Initializable {
    @FXML
    private PasswordField passwordField, rePasswordField;

    private String email;

    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email = DataHolder.getInstance().getData();
    }

    public void handleChangePassword() throws IOException {
        String newPassword = passwordField.getText().trim();
        String reEnteredPassword = rePasswordField.getText().trim();

        if (newPassword.equals(reEnteredPassword)) {
            if (!ValidationUtils.isValidPassword(newPassword)) {
                ControllerUtils.showAlertDialog("Password must be at least 8 characters.", Alert.AlertType.ERROR);
                return;
            }

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            userDao.updatePassword(email, hashedPassword);
            ControllerUtils.showAlertDialog("Đổi mật khẩu thành công", Alert.AlertType.INFORMATION);
            AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Mật khẩu không giống nhau", Alert.AlertType.ERROR);
        }
    }
}
