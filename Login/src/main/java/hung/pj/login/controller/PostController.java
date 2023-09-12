package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());

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
    private TableColumn<PostModel, Integer> creatorColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> createColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> updateColumn;

    // tự động cập nhật
    private ObservableList<PostModel> postModelObservableList = FXCollections.observableArrayList();

    // chuyển trang
    @FXML
    private void handleAddPost(ActionEvent event) {
        switchToScene("add_post.fxml", 1300, 750, false);
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_id"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView();

        // tạo menu edit
        ContextMenu menuPost = new ContextMenu();

        MenuItem delete_post = new MenuItem("DeletePost");
        delete_post.setOnAction(event -> {
            PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
            if (selectedPost != null) {
                int post_id = selectedPost.getPost_id();
                int deleted = postDao.deletePost(post_id);
                if (deleted == 1) {
                    refreshTableView();
                } else {
                    showAlert("Error", "Lỗi khi xóa post.", Alert.AlertType.ERROR);
                }
            }
        });
        MenuItem edit_post = new MenuItem("EditPost");
        edit_post.setOnAction(event -> {
            try {
                viewEditPost();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tableView.setContextMenu(menuPost);

        menuPost.getItems().addAll(edit_post,delete_post);

        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menuPost.hide();
            }
        });

    }

    // cập nhật dữ liệu
    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, postDao.getAllPosts());
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void viewEditPost() throws IOException {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            int selectedId = selectedPost.getPost_id();
            DataHolder.getInstance().setData(String.valueOf(selectedId));
            AppMain.setRoot("edit_post.fxml", 1300, 750, false);
        }
    }


}
