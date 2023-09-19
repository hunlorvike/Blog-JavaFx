package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.social.SocialDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView imageViewAvatar;
    @FXML
    private Label labelFileChoose;
    @FXML
    private Button butttonChooseFile;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField nameTextField, emailTextField, roleTextField, facebookTextField, instagramTextField, twitterTextField, pinterestTextField, githubTextField, gitlabTextField;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    SocialDaoImpl socialDao = new SocialDaoImpl(connectionProvider.getConnection());
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;

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
                // Load the default image if the avatarImage is still null
                avatarImage = new Image("@../image/newlogo.png");
            }

            System.out.println("Avatar Path: " + avatarPath);
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

    public void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Avatar");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(butttonChooseFile.getScene().getWindow());

        if (selectedFiles != null) {
            // Chọn chỉ tệp đầu tiên từ danh sách tệp đã chọn (bạn có thể điều chỉnh nếu cần)
            File selectedFile = selectedFiles.get(0);

            // Kiểm tra xem tệp đã chọn có đúng là hình ảnh không (bằng cách kiểm tra phần mở rộng hoặc kiểu MIME)
            if (isImageFile(selectedFile)) {
                // Hiển thị hình ảnh đã chọn trong ImageView
                Image selectedImage = new Image(selectedFile.toURI().toString());
                imageViewAvatar.setImage(selectedImage);

                // Hiển thị đường dẫn tệp đã chọn trong Label
                labelFileChoose.setText(selectedFile.getAbsolutePath());

                // Upload image lên database
                if(userDao.updateAvatar(selectedFile.getAbsolutePath(), loggedInUser.getUser_id())){
                    System.out.println("Upload thành công");
                }

            } else {
                // Nếu tệp không phải là hình ảnh, bạn có thể thông báo cho người dùng hoặc thực hiện xử lý khác tùy thuộc vào yêu cầu của bạn.
                labelFileChoose.setText("File is not an image.");
            }
        }
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif");
    }

}
