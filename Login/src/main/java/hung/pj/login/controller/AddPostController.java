package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField titleTextField, linkTextField;
    @FXML
    private TextArea contentTextField;
    @FXML
    private ChoiceBox<String> statusComboBox, categoryChoiceBox;
    @FXML
    private DatePicker datePicker;

    private final IPostDao postDao;
    private final UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;

    public AddPostController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        postDao = new PostDaoImpl(connectionProvider.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Xử lý sự kiện thay đổi giá trị trong ChoiceBox
        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            datePicker.setVisible("Scheduled".equals(newValue));
        });
    }

    public void handleAddPost() throws IOException {
        loggedInUser = userSingleton.getLoggedInUser();

        // Lấy dữ liệu
        int user_id = loggedInUser.getUser_id();
        String title = titleTextField.getText().trim();
        String content = contentTextField.getText().trim();
        String status = statusComboBox.getValue().trim();
        String link = linkTextField.getText().trim();
        String category = categoryChoiceBox.getValue().trim();

        // Tạo nội dung bài viết với cấu trúc mong muốn
        StringBuilder postContent = new StringBuilder(content);
        if (!link.isEmpty()) {
            postContent.append("\nBài viết: ").append(link);
        }

        Timestamp scheduledDate = null;

        if ("Scheduled".equals(status)) {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                scheduledDate = Timestamp.valueOf(selectedDate.atStartOfDay());
            }
        }

        PostModel post = new PostModel(title, content, status, scheduledDate, user_id, category);

        boolean insertSuccess = postDao.insertPost(post);
        if (insertSuccess) {
            ControllerUtils.showAlertDialog("Tạo bài viết thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
            AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Tạo bài viết thất bại", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
        }
    }
}
