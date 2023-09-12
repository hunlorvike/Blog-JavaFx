package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {
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
        int user_id = loggedInUser.getUser_id();
        String title = titleTextField.getText().trim();
        String content = contentTextField.getText().trim();
        String status = statusComboBox.getValue().trim();
        PostModel post = new PostModel(title, content, status, user_id);

        boolean insertSuccess = postDao.insertPost(post);
        if (insertSuccess) {
            ControllerUtils.showAlertDialog("Tạo bài viết thành công", Alert.AlertType.INFORMATION);
            AppMain.setRoot("post.fxml", 1300, 750, false);

        } else {
            ControllerUtils.showAlertDialog("Tạo bài viết thất bại", Alert.AlertType.INFORMATION);
        }

    }

}
