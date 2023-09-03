package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuUserController implements Initializable {
    private UserSingleton userSingleton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();
    }

    public void handleBackDashboard(ActionEvent event) {
        switchToScene("dashboard.fxml", 1300, 750, false);
    }

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
