package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ToolbarController implements Initializable {
    @FXML
    private Label labelDatetime, labelName;
    private UserSingleton userSingleton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Chuyển sang đối tượng Date để sử dụng SimpleDateFormat
        Date date = java.sql.Date.valueOf(currentDate);

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        labelDatetime.setText(String.valueOf(dateFormat));

        // Đặt định dạng và hiển thị trong Label
        labelDatetime.setText(dateFormat.format(date));

        // Đặt định dạng và hiển thị trong Label

        labelName.setText("Welcome back, " + loggedInUser.getFullname() + " - " + loggedInUser.getRole());
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSingleton.clearSingleton();

        if (userSingleton.getLoggedInUser() == null) {
            switchToScene("login.fxml", 1024, 600, false);
        }
    }

    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleProfile(ActionEvent event) {
        switchToScene("profile.fxml", 1300, 750, true);
    }
}
