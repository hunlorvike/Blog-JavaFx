package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.CustomListCell;
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

public class ResultSearchController implements Initializable {
    @FXML
    private ListView<UserModel> listViewResult;
    @FXML
    private Label searchLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lấy dữ liệu từ DataHolder
        List<UserModel> userModelList = DataHolder.getInstance().getDataList();
        String search = DataHolder.getInstance().getData();

        // Hiển thị kết quả tìm kiếm
        searchLabel.setText("Kết quả: " + search);

        // Thiết lập dữ liệu và giao diện cho ListView
        configureListView(userModelList);
        setupContextMenu();
    }

    private void configureListView(List<UserModel> userModelList) {
        // Gán danh sách dữ liệu cho ListView
        listViewResult.getItems().addAll(userModelList);

        // Gán giao diện từ item.fxml cho các mục trong ListView
        listViewResult.setCellFactory(param -> new CustomListCell());

        // Sử dụng ChangeListener để theo dõi sự thay đổi trong việc chọn mục
        listViewResult.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Lấy dữ liệu của mục đã chọn và thực hiện các xử lý cần thiết
                String email = newValue.getEmail();
                DataHolder.getInstance().setData(email);
            }
        });
    }

    private void setupContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem viewUserDetail = new MenuItem("Xem chi tiết");
        viewUserDetail.setOnAction(event -> viewUserDetail());

        listViewResult.setContextMenu(menu);
        menu.getItems().addAll(viewUserDetail);

        listViewResult.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menu.hide();
            }
        });
    }

    private void viewUserDetail() {
        try {
            AppMain.setRoot("profile_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT,false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
