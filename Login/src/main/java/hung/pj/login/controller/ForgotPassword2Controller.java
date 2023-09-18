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
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPassword2Controller implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    private PasswordField newPasswordField, reEnteredPasswordField;

    private String userEmail;

    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userEmail = DataHolder.getInstance().getData();
    }

    public void handleChangePassword() throws IOException {
        String newPasswordInput = newPasswordField.getText().trim();
        String reEnteredPasswordInput = reEnteredPasswordField.getText().trim();

        if (newPasswordInput.equals(reEnteredPasswordInput)) {
            if (!ValidationUtils.isValidPassword(newPasswordInput)) {
                ControllerUtils.showAlertDialog("Password must be at least 8 characters.", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
                return;
            }

            String hashedPassword = BCrypt.hashpw(newPasswordInput, BCrypt.gensalt());

            try {
                userDao.updatePassword(userEmail, hashedPassword);
                ControllerUtils.showAlertDialog("Đổi mật khẩu thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
            } catch (Exception e) {
                ControllerUtils.showAlertDialog("Đã xảy ra lỗi khi cập nhật mật khẩu.", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }
        } else {
            ControllerUtils.showAlertDialog("Mật khẩu không giống nhau", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
        }
    }
}
