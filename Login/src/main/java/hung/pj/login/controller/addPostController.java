package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addPostController implements Initializable {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea contentTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser; // Đặt biến loggedInUser ở mức lớp để nó có thể được truy cập từ các phương thức


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void handleAddPost(ActionEvent event) throws IOException {
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        // Lấy dữ liệu
        String title = titleTextField.getText();
        String content = contentTextField.getText();
        String status = statusComboBox.getValue();
        int user_id = loggedInUser.getUser_id();
        PostModel post = new PostModel(title, content, status, user_id);
//        postDao.insertPost(post);
        int insertSuccess = postDao.insertPost(post);
        if (insertSuccess == 1) {
            showAlert("Success", "Bài viết đã được thêm thành công.", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Error", "Đã xảy ra lỗi khi thêm bài viết.", Alert.AlertType.ERROR);
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


}
