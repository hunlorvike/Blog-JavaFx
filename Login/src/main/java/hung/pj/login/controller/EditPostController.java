package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.singleton.DataHolder;
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

public class EditPostController implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    private TextField titleTextField, linkTextField;
    @FXML
    private TextArea contentTextField;
    @FXML
    private ChoiceBox<String> statusComboBox, categoryChoiceBox;
    @FXML
    private DatePicker datePicker;

    private final IPostDao postDao;
    private int postId = -1;

    public EditPostController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        postDao = new PostDaoImpl(connectionProvider.getConnection());
    }

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

        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            datePicker.setVisible("Scheduled".equals(newValue));
        });
    }

    // Hiển thị giá trị cũ
    private void loadPostData(int postId) {
        PostModel selectedPost = postDao.getPostById(postId);
        if (selectedPost != null) {
            titleTextField.setText(selectedPost.getTitle());
            contentTextField.setText(selectedPost.getContent());
            statusComboBox.setValue(selectedPost.getStatus());
            categoryChoiceBox.setValue(selectedPost.getCategory());
        }
    }

    public void handleEditPost() throws IOException {
        if (postId >= 0) {
            PostModel existingPost = postDao.getPostById(postId);
            if (existingPost != null) {
                String title = titleTextField.getText().trim();
                String content = contentTextField.getText().trim();
                String status = statusComboBox.getValue().trim();
                String category = categoryChoiceBox.getValue().trim();
                Timestamp scheduledDate = null;

                if ("Scheduled".equals(status)) {
                    LocalDate selectedDate = datePicker.getValue();
                    if (selectedDate != null) {
                        scheduledDate = Timestamp.valueOf(selectedDate.atStartOfDay());
                    }
                }

                PostModel post = new PostModel(title, content, status, scheduledDate, category);

                boolean updateSuccess = postDao.updatePost(postId, post);
                if (updateSuccess) {
                    ControllerUtils.showAlertDialog("Sửa bài viết thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                    AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                } else {
                    ControllerUtils.showAlertDialog("Sửa bài viết thất bại", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
                }
            }
        }
    }
}
