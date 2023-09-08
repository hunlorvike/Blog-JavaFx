package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private Label nameLabel, emailLabel;
    String selectedEmail = DataHolder.getInstance().getData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserModel userModel = userDao.getUserByEmail(selectedEmail);
        nameLabel.setText(userModel.getFullname() + " 💢");
        emailLabel.setText(userModel.getEmail());
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
