package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDetailController implements Initializable {
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String selectedEmail = DataHolder.getInstance().getData();
        UserModel userModel = userDao.getUserByEmail(selectedEmail);
        nameLabel.setText(userModel.getFullname() + " 💢");
    }
}
