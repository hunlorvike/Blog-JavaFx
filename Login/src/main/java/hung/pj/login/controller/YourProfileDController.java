package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class YourProfileDController implements Initializable {
    private UserSingleton userSingleton;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();
    }

    public void handleClickSocial() {

    }
}
