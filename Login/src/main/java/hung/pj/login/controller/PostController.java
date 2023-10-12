package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.GridView;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton;
    UserModel loggedInUser;

    @FXML
    private Button allPostButton, publicPostButton, scheduledButton, draftPostButton, addPostButton;

    @FXML
    private TableView<PostModel> tableView;

    @FXML
    private TableColumn<PostModel, Integer> idColumn;

    @FXML
    private TableColumn<PostModel, String> titleColumn;

    @FXML
    private TableColumn<PostModel, String> statusColumn;

    @FXML
    private TableColumn<PostModel, Integer> viewColumn;

    @FXML
    private TableColumn<PostModel, String> creatorColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> createColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> updateColumn;

    @FXML
    private GridView listViewPost;

    private ObservableList<PostModel> postModelObservableList = FXCollections.observableArrayList();

    @FXML
    private void handleAddPost() {
        switchToScene("add_post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userSingleton = UserSingleton.getInstance();
        loggedInUser = userSingleton.getLoggedInUser();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_name"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView();

        ContextMenu menuPost = new ContextMenu();

        MenuItem savedPost = new MenuItem("Lưu xem sau");
        savedPost.setOnAction(event -> SavedIdPost());
        MenuItem deletePost = new MenuItem("Xoá bài viết");
        deletePost.setOnAction(event -> deletePost());
        MenuItem editPost = new MenuItem("Sửa bài viết");
        editPost.setOnAction(event -> {
            try {
                EditPost();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MenuItem detailPost = new MenuItem("Chi tiết bài viết");
        detailPost.setOnAction(event -> {
            try {
                DetailPost();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tableView.setContextMenu(menuPost);

        menuPost.getItems().addAll(editPost, detailPost, deletePost, savedPost);

        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menuPost.hide();
            }
        });
    }

    private void SavedIdPost() {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            // id post
            int selectedId = selectedPost.getPost_id();
//            id người dùng
            int userId = postDao.getUserIdForPost(selectedId);

            boolean save = postDao.insertSavedPost(userId, selectedId);
            if (save) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông báo", "Bài viết đã được lưu thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Thông báo", "Lưu bài viết không thành công!");
            }

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, postDao.getAllPosts());
    }

    private void EditPost() throws IOException {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost.getCreator_id() == loggedInUser.getUser_id() || loggedInUser.getRole().equals("Super Admin")) {
            int selectedId = selectedPost.getPost_id();
            DataHolder.getInstance().setData(String.valueOf(selectedId));
            AppMain.setRoot("edit_post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Bạn không có quyền sửa bài viết này", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }
        refreshTableView();
    }

    private void DetailPost() throws IOException {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            int selectedId = selectedPost.getPost_id();
            postDao.increaseViewCount(selectedId);
            DataHolder.getInstance().setData(String.valueOf(selectedId));
//            AppMain.setRoot("detail_post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
            AppMain.setRoot("post_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);

        }
        refreshTableView();
    }

    private void deletePost() {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost.getCreator_id() == loggedInUser.getUser_id() || loggedInUser.getRole().equals("Super Admin")) {
            postDao.deletePost(selectedPost.getPost_id());
            ControllerUtils.showAlertDialog("Xoá thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
        } else {
            ControllerUtils.showAlertDialog("Bạn không có quyền xoá bài viết này", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }
        refreshTableView();
    }

    public void handleAllButtonClicked(ActionEvent event) {
        refreshTableView();
    }

    public void handleStatusButtonClicked(ActionEvent event) {
        String status = "";
        String buttonId = ((Button) event.getSource()).getId();

        switch (buttonId) {
            case "publicPostButton":
                status = "Published";
                break;
            case "scheduledButton":
                status = "Scheduled";
                break;
            case "draftPostButton":
                status = "Draft";
                break;
            default:
                return;
        }

        List<PostModel> postModelList = postDao.getPostsByStatus(status);
        ControllerUtils.refreshTableView(tableView, postModelList);
    }
}
