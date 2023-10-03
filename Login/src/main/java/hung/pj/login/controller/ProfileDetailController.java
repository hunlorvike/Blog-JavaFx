package hung.pj.login.controller;

import com.jfoenix.controls.JFXToggleButton;
import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ProfileDetailController implements Initializable {

    @FXML
    private AnchorPane rootAnchorPane;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());
    @FXML
    private Label nameLabel, emailLabel, followLabel;
    @FXML
    private JFXToggleButton followToggleButton;
    private UserSingleton userSingleton;
    String selectedEmail = DataHolder.getInstance().getData();
    @FXML
    private TableView<PostModel> tableView;
    @FXML
    private TableColumn<PostModel, Integer> idColumn;
    @FXML
    private TableColumn<PostModel, String> titleColumn;
    @FXML
    private TableColumn<PostModel, String> statusColumn;
    @FXML
    private TableColumn<PostModel, Integer> viewColumn;
    @FXML
    private TableColumn<UserModel, Timestamp> createColumn;
    @FXML
    private TableColumn<UserModel, Timestamp> updateColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();
        userSingleton.setOnlineStatus(true);


        UserModel userModel = userDao.getUserByEmail(selectedEmail);

        boolean isFollowing = userDao.isFollowing(loggedInUser.getUser_id(), userModel.getUser_id());
        followToggleButton.setSelected(isFollowing);

        if (userModel.getUser_id() == loggedInUser.getUser_id()) {
            followToggleButton.setVisible(false);
        } else {
            followToggleButton.setVisible(true);
            if (isFollowing) {
                followToggleButton.setText("Đã theo dõi");
                followToggleButton.setTooltip(new Tooltip("Click để huỷ theo dõi"));
            } else {
                followToggleButton.setText("Theo dõi");
                followToggleButton.setTooltip(new Tooltip("Click để theo dõi"));
            }

            followToggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (followToggleButton.isSelected()) {
                    userDao.followUser(loggedInUser.getUser_id(), userModel.getUser_id());
                    followToggleButton.setText("Đã theo dõi");
                    followToggleButton.setTooltip(new Tooltip("Click để huỷ theo dõi"));
                    followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size() * 10 + " người theo dõi");
                } else {
                    userDao.unfollowUser(loggedInUser.getUser_id(), userModel.getUser_id());
                    followToggleButton.setText("Theo dõi");
                    followToggleButton.setTooltip(new Tooltip("Click để theo dõi"));
                    followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size() * 10 + " người theo dõi");
                }
            });
        }

        nameLabel.setText(userModel.getFullname() + " 💢");
        emailLabel.setText(userModel.getEmail());
        followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size() * 10 + " người theo dõi");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        // Tạo menu item
        MenuItem viewPostDetailMenuItem = new MenuItem("Xem chi tiết bài viết");
        viewPostDetailMenuItem.setOnAction(event -> {
            // Xử lý khi người dùng chọn xem chi tiết bài viết
            try {
                viewPostDetail();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Thêm menu item vào ContextMenu của tableViewPost
        ContextMenu postContextMenu = new ContextMenu();
        postContextMenu.getItems().add(viewPostDetailMenuItem);
        tableView.setContextMenu(postContextMenu);

        refreshTableView(userModel.getUser_id());
    }

    private void viewPostDetail() throws IOException {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            int selectedId = selectedPost.getPost_id();
            postDao.increaseViewCount(selectedId);
            DataHolder.getInstance().setData(String.valueOf(selectedId));
            AppMain.setRoot("post_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);

        }
    }

    private void refreshTableView(int user_id) {
        ControllerUtils.refreshTableView(tableView, postDao.getAllPostsByUserId(user_id));
    }

    public void handleClickSocial(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        int userId = userDao.getUserByEmail(selectedEmail).getUser_id();

        switch (imageView.getId()) {
            case "facebook":
                openSocialMediaProfile(userId, "Facebook");
                break;
            case "instagram":
                openSocialMediaProfile(userId, "Instagram");
                break;
            case "twitter":
                openSocialMediaProfile(userId, "Twitter");
                break;
            case "pinterest":
                openSocialMediaProfile(userId, "Pinterest");
                break;
            case "github":
                openSocialMediaProfile(userId, "Github");
                break;
            case "gitlab":
                openSocialMediaProfile(userId, "Gitlab");
                break;
            default:
                break;
        }
    }

    private void openSocialMediaProfile(int userId, String platform) {
        String profileUrl = socialDao.getProfileUrlByIdAndPlatform(userId, platform);
        if (profileUrl != null && !profileUrl.isEmpty()) {
            try {
                Desktop.getDesktop().browse(new URI(profileUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(platform + " không khả dụng.");
        }
    }
}
