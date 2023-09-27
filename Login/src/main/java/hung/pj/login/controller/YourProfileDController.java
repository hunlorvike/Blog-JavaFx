package hung.pj.login.controller;

import com.jfoenix.controls.JFXToggleButton;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class YourProfileDController implements Initializable {
    private UserSingleton userSingleton;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());

    @FXML
    private Label nameLabel, emailLabel, followLabel;
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

        UserModel userModel = userDao.getUserByEmail(loggedInUser.getEmail());

        nameLabel.setText(userModel.getFullname() + " 💢");
        emailLabel.setText(userModel.getEmail());
        followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size() * 10 + " người theo dõi");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView(userModel.getUser_id());
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
