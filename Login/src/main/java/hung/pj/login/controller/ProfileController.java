package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public TextField nameTextField, emailTextField, roleTextField, facebookTextField, instagramTextField, twitterTextField, pinterestTextField, githubTextField, gitlabTextField;
    private UserSingleton userSingleton;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        nameTextField.setText(loggedInUser.getFullname());
        emailTextField.setText(loggedInUser.getEmail());
        roleTextField.setText(loggedInUser.getRole());

        // Lấy thông tin xã hội từ cơ sở dữ liệu
        SocialModel facebookSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(), "Facebook");
        SocialModel instagramSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(),"Instagram");
        SocialModel twitterSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(),"Twitter");
        SocialModel pinterestSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(),"Pinterest");
        SocialModel githubSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(),"Github");
        SocialModel gitlabSocialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(),"Gitlab");

// Đặt giá trị của các TextField dựa trên thông tin xã hội
        facebookTextField.setText(facebookSocialMedia != null ? facebookSocialMedia.getProfileUrl() : "");
        instagramTextField.setText(instagramSocialMedia != null ? instagramSocialMedia.getProfileUrl() : "");
        twitterTextField.setText(twitterSocialMedia != null ? twitterSocialMedia.getProfileUrl() : "");
        pinterestTextField.setText(pinterestSocialMedia != null ? pinterestSocialMedia.getProfileUrl() : "");
        githubTextField.setText(githubSocialMedia != null ? githubSocialMedia.getProfileUrl() : "");
        gitlabTextField.setText(gitlabSocialMedia != null ? gitlabSocialMedia.getProfileUrl() : "");

    }
}
