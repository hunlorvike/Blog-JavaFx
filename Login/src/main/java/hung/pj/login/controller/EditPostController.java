package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditPostController implements Initializable {
    private int postId;
    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea contentTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());


    public void handleEditPost(ActionEvent event) throws IOException {
        // lấy post_id từ DataHolder
        String postId = DataHolder.getInstance().getData();

        // Check postId
        if (postId != null) {
            PostModel existingPost = postDao.getPostById(Integer.parseInt(postId));
            if (existingPost != null) {

                int id = Integer.parseInt(postId);
                String title = titleTextField.getText().trim();
                String content = contentTextField.getText().trim();
                String status = statusComboBox.getValue().trim();

                existingPost.setPost_id(id);
                existingPost.setTitle(title);
                existingPost.setContent(content);
                existingPost.setStatus(status);

                int updateSuccess = postDao.updatePost(existingPost);

                if (updateSuccess == 1) {
                    showAlert("Success", "Bài viết đã được cập nhật thành công.", Alert.AlertType.INFORMATION);
                    clearFields();
                } else {
                    showAlert("Error", "Lỗi rồi.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Không tìm thấy bài viết để cập nhật.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Không có thông tin bài viết để cập nhật.", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void clearFields() {
        titleTextField.clear();
        contentTextField.clear();
        statusComboBox.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String postIdString = DataHolder.getInstance().getData();
        if (postIdString != null) {
            postId = Integer.parseInt(postIdString);
            loadPostData(postId);
        }
    }
// hiển thị giá trị old
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

}
