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
import hung.pj.login.ultis.ValidationUtils;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import hung.pj.login.AppMain;
import javafx.util.Duration;

public class LoginController implements Initializable {

    @FXML
    public Label signupLabel;
    public TextField emailTextField;
    public PasswordField passwordField;
    public Label alertMessage;
    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleClickLogin(ActionEvent actionEvent) {
        try {
            String email = emailTextField.getText().trim();
            String password = passwordField.getText().trim();

            if (!ValidationUtils.isValidEmail(email)) {
                showAlert("Invalid email address.");
                return;
            }

            UserModel user = userDao.getUserByEmail(email);

            if (user != null) {
                if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
                    // Tài khoản bị khoá và thời gian khoá chưa kết thúc
                    showAlert("Account is locked. Please try again later.");
                } else {
                    if (userDao.authenticateUser(email, password)) {
                        UserSingleton userSingleton = UserSingleton.getInstance();
                        userSingleton.setLoggedInUser(user);
                        // Sử dụng splash khi chuyển đến dashboard.fxml
                        AppMain.setRoot("dashboard.fxml", 1300, 750, true);
                    } else {
                        showAlert("Incorrect password.");
                    }
                }
            } else {
                showAlert("User not found.");
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

    // Chuyển trang
    public void handleClickSignUp(MouseEvent mouseEvent) throws IOException {
        AppMain.setRoot("signup.fxml", 1024, 600, false);
    }
}
