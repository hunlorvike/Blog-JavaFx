package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField nameTextField, emailTextField, roleTextField, facebookTextField, instagramTextField, twitterTextField, pinterestTextField, githubTextField, gitlabTextField;
    private final UserSingleton userSingleton = UserSingleton.getInstance();
    ConnectionProvider connectionProvider = new ConnectionProvider();
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    private UserModel loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInUser = userSingleton.getLoggedInUser();

        nameTextField.setText(loggedInUser.getFullname());
        emailTextField.setText(loggedInUser.getEmail());
        roleTextField.setText(loggedInUser.getRole());

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

        String facebook = facebookTextField.getText().trim();
        String instagram = instagramTextField.getText().trim();
        String twitter = twitterTextField.getText().trim();
        String pinterest = pinterestTextField.getText().trim();
        String github = githubTextField.getText().trim();
        String gitlab = gitlabTextField.getText().trim();

        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Facebook", facebook);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Instagram", instagram);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Twitter", twitter);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Pinterest", pinterest);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Github", github);
        checkAndUpdateSocialMedia(loggedInUser.getUser_id(), "Gitlab", gitlab);

        ControllerUtils.showAlertDialog("Lưu thông tin thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
    }

    private void checkAndUpdateSocialMedia(int userId, String platform, String profileUrl) {
        if (!profileUrl.isEmpty()) {
            SocialModel existingSocialMedia = socialDao.getSocialMediaByPlatform(userId, platform);

            if (existingSocialMedia != null) {
                if (!profileUrl.equals(existingSocialMedia.getProfileUrl())) {
                    existingSocialMedia.setProfileUrl(profileUrl);
                    socialDao.updateSocialMedia(existingSocialMedia.getUserId(), existingSocialMedia);
                }
            } else {
                List<SocialModel> socialModels = new ArrayList<>();
                socialModels.add(new SocialModel(userId, platform, profileUrl));
                socialDao.addSocialMedia(socialModels);
            }
        }
    }
}
