package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.LoginInfo;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import hung.pj.login.AppMain;
import hung.pj.login.utils.ValidationUtils;
import hung.pj.login.utils.RememberMeManager;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private CheckBox rememberMeCheckBox;
    @FXML
    private Label signupLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label alertMessage;
    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private ObservableList<LoginInfo> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<LoginInfo> loginInfoList = RememberMeManager.getSavedLoginInfoList();
        if (!loginInfoList.isEmpty()) {
            LoginInfo firstLoginInfo = loginInfoList.get(0); // Lấy phần tử đầu tiên
            String username = firstLoginInfo.getUsername(); // Lấy tên tài khoản
            String password = firstLoginInfo.getPassword(); // Lấy mật khẩu
            // Gán dữ liệu vào các ô input tài khoản và mật khẩu
            emailTextField.setText(username);
            passwordField.setText(password);
        }
    }


    public void handleClickLogin() {
        try {
            String email = emailTextField.getText().trim();
            String password = passwordField.getText().trim();

            if (!ValidationUtils.isValidEmail(email)) {
                showAlert("Email không hợp lệ.");
                return;
            }

            UserModel user = userDao.getUserByEmail(email);

            if (user != null) {
                if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
                    // Tài khoản bị khoá và thời gian khoá chưa kết thúc
                    showAlert("Tài khoản bị khoá. Vui lòng thử lại sau.");
                } else {
                    if (userDao.authenticateUser(email, password)) {
                        UserSingleton userSingleton = UserSingleton.getInstance();
                        userSingleton.setLoggedInUser(user);

                        // Kiểm tra xem rememberMeCheckBox đã được chọn
                        if (rememberMeCheckBox.isSelected()) {
                            // Lưu thông tin đăng nhập vào tệp rememberme.properties
                            RememberMeManager.saveLoginInfo(email, password);
                        }

                        // Sử dụng splash khi chuyển đến dashboard.fxml
                        AppMain.setRoot("dashboard.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, true);
                    } else {
                        showAlert("Sai mật khẩu.");
                    }
                }
            } else {
                showAlert("Không tìm thấy người dùng.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        alertMessage.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), timelineEvent -> {
                    alertMessage.setText(null);
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    // Chuyển trang đăng ký
    public void handleClickSignUp() throws IOException {
        AppMain.setRoot("signup.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
    }

    // Chuyển trang quên mật khẩu
    public void handleClickForgotPass() throws IOException {
        AppMain.setRoot("forgot_password.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
    }
}
