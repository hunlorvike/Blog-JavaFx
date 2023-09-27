package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.SavedPost.SavedPostDao;
import hung.pj.login.dao.SavedPost.SavedPostDaoImpl;
import hung.pj.login.model.SavedPostModel;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SavedController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TableView<SavedPostModel> tableView;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    SavedPostDao SavedPostDao = new SavedPostDaoImpl(connectionProvider.getConnection());
    @FXML
    private TableColumn<SavedPostModel, Integer> idColumn;

    @FXML
    private TableColumn<SavedPostModel, Integer> titleColumn;

    @FXML
    private TableColumn<SavedPostModel, Integer> creatorColumn;

    ObservableList<SavedPostModel> savedPostsList = FXCollections.observableArrayList();

    @FXML
    private void handleAddSaved() {
        switchToScene("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("saved_post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("postTitle"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creatorUsername"));
        refreshTableView();
    }

    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, SavedPostDao.getAllSavedPosts());
    }

}
