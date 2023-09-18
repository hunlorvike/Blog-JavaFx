package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.ultis.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;

import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());


    @FXML
    private Label numberOfUsersLabel, numberOfSuperAdmin, numberOfAdmin, numberOfModerator, numberOfAllPosts, numberOfPublished, numberOfScheduled, numberOfDrafts;

    @FXML
    private TableView<PostModel> tableViewPost;

    @FXML
    private TableColumn<PostModel, Integer> idColumn, idMemberColumn, viewColumn, creatorColumn;

    @FXML
    private TableColumn<PostModel, String> titleColumn, statusColumn, categoryColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> createdAtColumn, updatedAtColumn;

    @FXML
    private TableView<UserModel> tableViewMember;

    @FXML
    private TableColumn<UserModel, String> nameColumn, postColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_id"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTablePostView();

        setDataTabPost();
        setDataTabMember();
        setDataTableMember();
    }

    private void refreshTablePostView() {
        ControllerUtils.refreshTableView(tableViewPost, postDao.getAllPosts());
    }

    private void setDataTabPost() {
        int totalPostsCount = postDao.getAllPosts().size();
        setLabelValue(numberOfAllPosts, totalPostsCount + " Posts");

        int publicPostsCount = postDao.getPostsByStatus("Published").size();
        setLabelValue(numberOfPublished, String.valueOf(publicPostsCount));

        int scheduledPostsCount = postDao.getPostsByStatus("Scheduled").size();
        setLabelValue(numberOfScheduled, String.valueOf(scheduledPostsCount));

        int draftPostsCount = postDao.getPostsByStatus("Draft").size();
        setLabelValue(numberOfDrafts, String.valueOf(draftPostsCount));


    }

    private void setDataTabMember() {
        int totalUsersCount = userDao.getAllUsers().size();
        setLabelValue(numberOfUsersLabel, totalUsersCount + " Members");

        int superAdminCount = userDao.getUsersByRole("Super Admin").size();
        setLabelValue(numberOfSuperAdmin, String.valueOf(superAdminCount));

        int adminCount = userDao.getUsersByRole("Admin").size();
        setLabelValue(numberOfAdmin, String.valueOf(adminCount));

        int moderatorCount = userDao.getUsersByRole("Moderator").size();
        setLabelValue(numberOfModerator, String.valueOf(moderatorCount));
    }

    private void setDataTableMember() {
        idMemberColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("postCount"));

        refreshTableViewMember();
    }

    private void refreshTableViewMember() {
        ControllerUtils.refreshTableView(tableViewMember, userDao.getUsersByPostCountDescending(15));
    }

    private void setLabelValue(Label label, String text) {
        label.setText(text);
    }
}