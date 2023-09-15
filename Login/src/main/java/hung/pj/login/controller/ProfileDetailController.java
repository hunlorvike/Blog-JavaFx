package hung.pj.login.controller;

import com.jfoenix.controls.JFXToggleButton;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileDetailController implements Initializable {

    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    @FXML
    private Label nameLabel, emailLabel, followLabel;
    @FXML
    private JFXToggleButton followToggleButton;
    private UserSingleton userSingleton;

    String selectedEmail = DataHolder.getInstance().getData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        UserModel userModel = userDao.getUserByEmail(selectedEmail);

        // Đặt giá trị ban đầu cho nút toggle
        boolean isFollowing = userDao.isFollowing(loggedInUser.getUser_id(), userModel.getUser_id());
        followToggleButton.setSelected(isFollowing);

        if (userModel.getUser_id() == loggedInUser.getUser_id()) {
            // Ẩn nút theo dõi nếu đang xem trang cá nhân của chính mình
            followToggleButton.setVisible(false);
        } else {
            followToggleButton.setVisible(true);
            if (isFollowing) {
                // Nếu đã follow, đặt biểu tượng và tooltip tương ứng
                followToggleButton.setText("Đã theo dõi");
                followToggleButton.setTooltip(new Tooltip("Click để huỷ theo dõi"));
            } else {
                // Nếu chưa follow, đặt biểu tượng và tooltip tương ứng
                followToggleButton.setText("Theo dõi");
                followToggleButton.setTooltip(new Tooltip("Click để theo dõi"));
            }

            followToggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (followToggleButton.isSelected()) {
                    System.out.println(followToggleButton.isSelected());
                    userDao.followUser(loggedInUser.getUser_id(), userModel.getUser_id());
                    followToggleButton.setText("Đã theo dõi");
                    followToggleButton.setTooltip(new Tooltip("Click để huỷ theo dõi"));
                    followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size()*10  + " người theo dõi");
                } else {
                    System.out.println(followToggleButton.isSelected());
                    userDao.unfollowUser(loggedInUser.getUser_id(), userModel.getUser_id());
                    followToggleButton.setText("Theo dõi");
                    followToggleButton.setTooltip(new Tooltip("Click để theo dõi"));
                    followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size()*10 + " người theo dõi");

                }
                if (newValue) {

                } else {

                }
            });
        }

        nameLabel.setText(userModel.getFullname() + " 💢");
        emailLabel.setText(userModel.getEmail());
        followLabel.setText("Có " + userDao.getFollowing(userModel.getUser_id()).size()*10 + " người theo dõi");
    }


    public void handleClickSocial(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();

        // Lấy userId từ selectedEmail
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
