package hung.pj.login.controller;

import com.browniebytes.javafx.control.DateTimePicker;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {

    @FXML
    private TextField titleTextField, linkTextField;
    @FXML
    private TextArea contentTextField;
    @FXML
    private ChoiceBox<String> statusComboBox, tagComboBox;
    @FXML
    private DatePicker datePicker;

//    @FXML
//    private ComboBox<String> statusComboBox;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser; // Đặt biến loggedInUser ở mức lớp để nó có thể được truy cập từ các phương thức


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Xử lý sự kiện thay đổi giá trị trong ChoiceBox
        statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("Scheduled".equals(newValue)) {
                    // Nếu người dùng chọn "Scheduled", hiển thị DatePicker
                    datePicker.setVisible(true);
                } else {
                    // Nếu người dùng chọn bất kỳ giá trị khác, ẩn DatePicker
                    datePicker.setVisible(false);
                }
            }
        });
    }


    public void handleAddPost() throws IOException {
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        // Lấy dữ liệu
        int user_id = loggedInUser.getUser_id();
        String title = titleTextField.getText().trim();
        String content = contentTextField.getText().trim();
        String status = statusComboBox.getValue().trim();
        String link = linkTextField.getText().trim();

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

        PostModel post = new PostModel(title, postContent.toString(), status, scheduledDate, user_id);

        boolean insertSuccess = postDao.insertPost(post);
        if (insertSuccess) {
            ControllerUtils.showAlertDialog("Tạo bài viết thành công", Alert.AlertType.INFORMATION);
            AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Tạo bài viết thất bại", Alert.AlertType.INFORMATION);
        }
    }



}
