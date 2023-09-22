package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ImageFileUtil;
import hung.pj.login.ultis.Constants; // Import Constants
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView imageViewAvatar;
    @FXML
    private Label labelFileChoose;
    @FXML
    private Button buttonChooseFile;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField nameTextField, emailTextField, roleTextField, facebookTextField, instagramTextField, twitterTextField, pinterestTextField, githubTextField, gitlabTextField;
    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;
    private String selectedFilePath = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userSingleton = UserSingleton.getInstance();
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

        UserModel user = userDao.getUserById(loggedInUser.getUser_id());
        if (user != null) {
            String avatarPath = user.getAvatarPath();
            Image avatarImage = null;

            if (avatarPath != null) {
                File file = new File(avatarPath);
                if (file.exists()) {
                    avatarImage = new Image(file.toURI().toString());
                }
            }

            if (avatarImage != null) {
                imageViewAvatar.setImage(avatarImage);
            } else {
                avatarImage = new Image(getClass().getResource("/hung/pj/login/image/newlogo.png").toExternalForm());
            }
        } else {
            System.out.println("Không tìm thấy người dùng.");
        }
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

        if (selectedFilePath != null) {
            String storageDirectoryPath = Constants.UPLOAD_DIRECTORY; // Sử dụng hằng số UPLOAD_DIRECTORY
            File storageDirectory = new File(storageDirectoryPath);

            if (!storageDirectory.exists()) {
                storageDirectory.mkdirs();
            }

            String destinationPath = storageDirectoryPath + File.separator + new File(selectedFilePath).getName();
            File destinationFile = new File(destinationPath);

            try {
                Files.copy(Paths.get(selectedFilePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

                Image selectedImage = new Image(destinationFile.toURI().toString());
                imageViewAvatar.setImage(selectedImage);

                if (userDao.updateAvatar(destinationPath, loggedInUser.getUser_id())) {
                    System.out.println("Upload thành công");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            labelFileChoose.setText("Please choose a file first.");
        }
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

    public void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Avatar");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(buttonChooseFile.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            File selectedFile = selectedFiles.get(0);

            if (ImageFileUtil.isImageFile(selectedFile)) {
                selectedFilePath = selectedFile.getAbsolutePath();
                labelFileChoose.setText(selectedFilePath);
            } else {
                labelFileChoose.setText("File is not an image.");
            }
        }
    }
}
