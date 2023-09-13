package hung.pj.login.controller;


import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
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
    private Label numberOfUsersLabel, numberOfSuperAdmin, numberOfAdmin, numberOfModerator, numberOfAllPosts, numberOfPublished, numberOfScheduled, numberOfDrafts; // Đối tượng Label trên giao diện người dùng

    @FXML
    private TableView<PostModel> tableViewPost;

    @FXML
    private TableColumn<PostModel, Integer> idColumn;

    @FXML
    private TableColumn<PostModel, String> titleColumn;

    @FXML
    private TableColumn<PostModel, String> statusColumn;

    @FXML
    private TableColumn<PostModel, Integer> viewColumn;

    @FXML
    private TableColumn<PostModel, Integer> creatorColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> createColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> updateColumn;

    @FXML
    private TableView<UserModel> tableViewMember;

    @FXML
    private TableColumn<UserModel, String> nameColumn;

    @FXML
    private TableColumn<UserModel, String> postColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataTabPost();

        setDataTabMember();

        setDataTableMember();
    }

    public void setDataTabPost() {
        List<PostModel> allPosts = postDao.getAllPosts();
        int numberAllPosts = allPosts.size();
        setLabelValue(numberOfAllPosts, numberAllPosts + " Posts");

        List<PostModel> publicPosts = postDao.getPostsByStatus("Published".trim());
        int numberPublicPosts = publicPosts.size();
        setLabelValue(numberOfPublished, String.valueOf(numberPublicPosts));

        List<PostModel> scheduledPosts = postDao.getPostsByStatus("Scheduled".trim());
        int numberScheduledPosts = scheduledPosts.size();
        setLabelValue(numberOfScheduled, String.valueOf(numberScheduledPosts));

        List<PostModel> draftPosts = postDao.getPostsByStatus("Draft".trim());
        int numberDraftPosts = draftPosts.size();
        setLabelValue(numberOfDrafts, String.valueOf(numberDraftPosts));
    }

    public void setDataTabMember() {
        List<UserModel> allUsers = userDao.getAllUsers();
        int numberOfUsers = allUsers.size();
        setLabelValue(numberOfUsersLabel, numberOfUsers + " Members");

        List<UserModel> usersSuperAdmin = userDao.getUsersByRole("Super Admin".trim());
        int numberOfUsersSuperAdmin = usersSuperAdmin.size();
        setLabelValue(numberOfSuperAdmin, String.valueOf(numberOfUsersSuperAdmin));

        List<UserModel> usersAdmin = userDao.getUsersByRole("Admin".trim());
        int numberOfUsersAdmin = usersAdmin.size();
        setLabelValue(numberOfAdmin, String.valueOf(numberOfUsersAdmin));

        List<UserModel> usersModerator = userDao.getUsersByRole("Moderator".trim());
        int numberOfUsersModerator = usersModerator.size();
        setLabelValue(numberOfModerator, String.valueOf(numberOfUsersModerator));
    }

    public void setDataTablePost() {

    }

    public void setDataTableMember() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("postCount"));
        refreshTableViewMember();
    }

    private void refreshTableViewMember() {
        ControllerUtils.refreshTableView(tableViewMember, userDao.getUsersByPostCountDescending(10));
    }

    private void setLabelValue(Label label, String text) {
        label.setText(text);
    }
}

