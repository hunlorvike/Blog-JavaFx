package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private TextField nameTextField, emailTextField, roleTextField, facebookTextField, instagramTextField, twitterTextField, pinterestTextField, githubTextField, gitlabTextField;
    private UserSingleton userSingleton = UserSingleton.getInstance();
    ConnectionProvider connectionProvider = new ConnectionProvider();
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    private UserModel loggedInUser; // Đặt biến loggedInUser ở mức lớp để nó có thể được truy cập từ các phương thức

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        loggedInUser = userSingleton.getLoggedInUser();

        // Đặt giá trị của các TextField dựa trên thông tin người dùng
        nameTextField.setText(loggedInUser.getFullname());
        emailTextField.setText(loggedInUser.getEmail());
        roleTextField.setText(loggedInUser.getRole());

        // Lấy thông tin xã hội từ cơ sở dữ liệu và đặt giá trị của các TextField
        setSocialMediaTextField("Facebook", facebookTextField);
        setSocialMediaTextField("Instagram", instagramTextField);
        setSocialMediaTextField("Twitter", twitterTextField);
        setSocialMediaTextField("Pinterest", pinterestTextField);
        setSocialMediaTextField("Github", githubTextField);
        setSocialMediaTextField("Gitlab", gitlabTextField);
    }

    private void setSocialMediaTextField(String platform, TextField textField) {
        SocialModel socialMedia = socialDao.getSocialMediaByPlatform(loggedInUser.getUser_id(), platform);
        textField.setText(socialMedia != null ? socialMedia.getProfileUrl() : "");
    }

    public void handleClickSave(ActionEvent event) {
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        // Lấy thông tin từ các TextField và loại bỏ khoảng trắng không cần thiết
        String facebook = facebookTextField.getText().trim();
        String instagram = instagramTextField.getText().trim();
        String twitter = twitterTextField.getText().trim();
        String pinterest = pinterestTextField.getText().trim();
        String github = githubTextField.getText().trim();
        String gitlab = gitlabTextField.getText().trim();

        // Kiểm tra và cập nhật hoặc thêm dữ liệu xã hội vào cơ sở dữ liệu
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Facebook", facebook);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Instagram", instagram);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Twitter", twitter);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Pinterest", pinterest);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Github", github);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Gitlab", gitlab);
    }

    private void checkAndUpdateSocialMedia(int userId, String platform, String profileUrl) {
        if (!profileUrl.isEmpty()) {
            SocialModel existingSocialMedia = socialDao.getSocialMediaByPlatform(userId, platform);

            if (existingSocialMedia != null) {
                // Nếu bản ghi đã tồn tại, kiểm tra xem profileUrl có thay đổi hay không
                if (!profileUrl.equals(existingSocialMedia.getProfileUrl())) {
                    // Nếu có sự thay đổi, cập nhật profileUrl trong cơ sở dữ liệu
                    existingSocialMedia.setProfileUrl(profileUrl);
                    socialDao.updateSocialMedia(existingSocialMedia.getUserId(), existingSocialMedia);
                }
            } else {
                // Nếu không có bản ghi nào tồn tại, thêm một bản ghi mới vào cơ sở dữ liệu
                List<SocialModel> socialModels = new ArrayList<>();
                socialModels.add(new SocialModel(userId, platform, profileUrl));

                // Pass the list to the addSocialMedia method
                socialDao.addSocialMedia(socialModels);
            }
        }
    }



}
