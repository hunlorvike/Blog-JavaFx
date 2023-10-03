package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.savedPost.SavedPostDao;
import hung.pj.login.dao.savedPost.SavedPostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class SavedController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TableView<PostModel> tableView;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    SavedPostDao SavedPostDao = new SavedPostDaoImpl(connectionProvider.getConnection());
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());

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

    ObservableList<PostModel> savedPostsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("saved_post_id")); // Update to "id"
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_id"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView();

        ContextMenu menuSavedPost = new ContextMenu();
        MenuItem deleteSavedPost = new MenuItem("Xoá lưu bài viết");
        deleteSavedPost.setOnAction(event -> deleteSavedPost());
        MenuItem detailPost = new MenuItem("Chi tiết bài viết");
        detailPost.setOnAction(event -> {
            try {
                detailPost();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menuSavedPost.getItems().addAll(detailPost, deleteSavedPost);

        tableView.setContextMenu(menuSavedPost);

        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menuSavedPost.hide();
            }
        });
    }


    private void detailPost() throws IOException {
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

    private void deleteSavedPost() {
        PostModel selectedPost = tableView.getSelectionModel().getSelectedItem();
        SavedPostDao.deleteSavedPost(selectedPost.getSaved_post_id());
        refreshTableView();

    }


    @FXML
    private void handleAddSaved() {
        switchToScene("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }


    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, SavedPostDao.getAllSavedPosts());
    }

    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
