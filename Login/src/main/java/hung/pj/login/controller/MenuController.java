package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private UserSingleton userSingleton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();
    }

    public void handleSwitchDashboard() {
        switchToScene("dashboard.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    public void handleSwitchPost() {
        switchToScene("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    public void handleSwitchCategory() {
        switchToScene("category.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    public void handleSwitchMember() {
        switchToScene("member.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    public void handleSwitchMessenger() {
        switchToScene("messenger.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    // Phương thức dùng chung cho việc switch-case chuyển trang
    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHelper(ActionEvent actionEvent) {
        try {
            // Mở trình duyệt mặc định và chuyển đến đường dẫn
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/messages/t/100043588192736"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
