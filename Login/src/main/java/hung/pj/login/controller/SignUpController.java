package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import hung.pj.login.ultis.ValidationUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public TextField nameTextField, emailTextField;
    public PasswordField passwordField, repasswordField;
    public CheckBox checkbox;
    public AnchorPane rootAnchorPaneSignUp;
    public Button buttonSignUp;
    public Label alertMessage;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        repasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean passwordsMatch = passwordField.getText().equals(newValue);
            buttonSignUp.setDisable(!passwordsMatch);

            if (!passwordsMatch) {
                ValidationUtils.setRedBorder(repasswordField);
                alertMessage.setText("Passwords do not match");
            } else {
                ValidationUtils.removeBorder(repasswordField);
                alertMessage.setText("");
            }
        });
    }

    public void handleClickLogin(MouseEvent mouseEvent) throws IOException {
        AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT,  false);
    }

    public void handleClickSignUp(ActionEvent event) throws IOException {
        String fullname = nameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String password = passwordField.getText().trim();
        String repassword = repasswordField.getText().trim();
        boolean agreedToTerms = checkbox.isSelected();

        if (!agreedToTerms) {
            showAlert("You must agree to the terms and conditions before signing up.");
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            showAlert("Invalid email address.");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            showAlert("Password must be at least 8 characters.");
            return;
        }

        if (!password.equals(repassword)) {
            showAlert("Passwords do not match.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String role = "Moderator";
        UserModel user = new UserModel(fullname, email, hashedPassword, role);
        userDao.insertUser(user);

        clearFields();
        ControllerUtils.showAlertDialog("Đã tạo tài khoản thành công", Alert.AlertType.INFORMATION);
        AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT,  false);

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

    private void clearFields() {
        nameTextField.clear();
        emailTextField.clear();
        passwordField.clear();
        repasswordField.clear();
    }
}
