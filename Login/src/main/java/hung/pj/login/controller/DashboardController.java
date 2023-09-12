package hung.pj.login.controller;


import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
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
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());

    @FXML
    private Label numberOfUsersLabel, numberOfSuperAdmin, numberOfAdmin, numberOfModerator, numberOfTaks, numberOfScheduled; // Đối tượng Label trên giao diện người dùng


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<UserModel> allUsers = userDao.getAllUsers();
        int numberOfUsers = allUsers.size();

        List<UserModel> usersSuperAdmin = userDao.getUsersByRole("Super Admin".trim());
        int numberOfUsersSuperAdmin = usersSuperAdmin.size();

        List<UserModel> usersAdmin = userDao.getUsersByRole("Admin".trim());
        int numberOfUsersAdmin = usersAdmin.size();

        List<UserModel> usersModerator = userDao.getUsersByRole("Moderator".trim());
        int numberOfUsersModerator = usersModerator.size();

        // Cập nhật Label để hiển thị số lượng người dùng
        numberOfUsersLabel.setText(String.valueOf(numberOfUsers) + " Members");
        numberOfSuperAdmin.setText(String.valueOf(numberOfUsersSuperAdmin));
        numberOfAdmin.setText(String.valueOf(numberOfUsersAdmin));
        numberOfModerator.setText(String.valueOf(numberOfUsersModerator));


    }
}

