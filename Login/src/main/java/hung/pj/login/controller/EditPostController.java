package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditPostController implements Initializable {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea contentTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());
    int postId = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String postIdString = DataHolder.getInstance().getData();
        if (postIdString != null && !postIdString.isEmpty()) {
            try {
                postId = Integer.parseInt(postIdString);
            } catch (NumberFormatException e) {
                postId = -1;
            }
        }
        if (postId >= 0) {
            loadPostData(postId);
        }
    }

    // Hiển thị giá trị cũ
    private void loadPostData(int postId) {
        PostModel selectedPost = postDao.getPostById(postId);
        if (selectedPost != null) {
            String title = selectedPost.getTitle();
            String content = selectedPost.getContent();
            String status = selectedPost.getStatus();

            titleTextField.setText(title);
            contentTextField.setText(content);
            statusComboBox.setValue(status);
        }
    }

    public void handleEditPost(ActionEvent event) throws IOException {
        if (postId >= 0) {
            PostModel existingPost = postDao.getPostById(postId);
            if (existingPost != null) {
                String title = titleTextField.getText().trim();
                String content = contentTextField.getText().trim();
                String status = statusComboBox.getValue().trim();

                existingPost.setTitle(title);
                existingPost.setContent(content);
                existingPost.setStatus(status);

                boolean updateSuccess = postDao.updatePost(postId, existingPost);

                if (updateSuccess) {
                    ControllerUtils.showAlertDialog("Sửa bài viết thành công", Alert.AlertType.INFORMATION);
                    AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                } else {
                    ControllerUtils.showAlertDialog("Sửa bài viết thất bại", Alert.AlertType.ERROR);
                }
            }
        }
    }
}
