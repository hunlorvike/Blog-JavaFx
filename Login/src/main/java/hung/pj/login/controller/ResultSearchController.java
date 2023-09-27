package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.CustomListCell;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultSearchController<T extends Object> implements Initializable {
    @FXML
    private ListView<T> listViewResult;
    @FXML
    private Label searchLabel;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<T> modelList = DataHolder.getInstance().getDataList();
        String search = DataHolder.getInstance().getData();

        // Hiển thị kết quả tìm kiếm
        searchLabel.setText("Kết quả: " + search);

        // Thiết lập dữ liệu và giao diện cho ListView
        configureListView(modelList);
        setupContextMenu();
    }


    private void configureListView(List<T> model) {
        // Gán danh sách dữ liệu cho ListView
        listViewResult.getItems().addAll(model);

        // Gán giao diện từ item.fxml cho các mục trong ListView
        listViewResult.setCellFactory(param -> new CustomListCell<T>());

        // Sử dụng ChangeListener để theo dõi sự thay đổi trong việc chọn mục
        listViewResult.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Lấy dữ liệu của mục đã chọn và thực hiện các xử lý cần thiết
                T selectedItem = listViewResult.getSelectionModel().getSelectedItem();
                System.out.println("Xem chi tiết : " + selectedItem);

            }
        });
    }

    private void setupContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem viewUserDetail = new MenuItem("Xem chi tiết");
        viewUserDetail.setOnAction(event -> {
            try {
                viewDetail();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        listViewResult.setContextMenu(menu);
        menu.getItems().addAll(viewUserDetail);

        listViewResult.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                try {
                    viewDetail();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void viewDetail() throws IOException {
        T selectedItem = listViewResult.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem instanceof UserModel) {
                UserModel userModel = (UserModel) selectedItem;
                if (userModel != null) { // Check if userModel is not null
                    DataHolder.getInstance().setData(String.valueOf(userModel.getEmail()));
                    AppMain.setRoot("profile_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                }
            } else if (selectedItem instanceof PostModel) {
                PostModel postModel = (PostModel) selectedItem;
                if (postModel != null) { // Check if postModel is not null
                    DataHolder.getInstance().setData(String.valueOf(postModel.getPost_id()));
                    postDao.increaseViewCount(postModel.getPost_id());
                    AppMain.setRoot("post_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                }
            }
        }
    }


}
