package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ToolbarController implements Initializable {
    @FXML
    private Label labelDatetime, labelName;
    private UserSingleton userSingleton;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextField textField;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Chuyển sang đối tượng Date để sử dụng SimpleDateFormat
        Date date = java.sql.Date.valueOf(currentDate);

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        labelDatetime.setText(String.valueOf(dateFormat));

        // Đặt định dạng và hiển thị trong Label
        labelDatetime.setText(dateFormat.format(date));

        // Đặt định dạng và hiển thị trong Label

        labelName.setText("Welcome back, " + loggedInUser.getFullname() + " - " + loggedInUser.getRole());
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSingleton.clearSingleton();

        if (userSingleton.getLoggedInUser() == null) {
            switchToScene("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT,  false);
        }
    }

    public void handleSearch() {
        // Lấy giá trị từ ChoiceBox và TextField
        String selectedValue = choiceBox.getValue().trim();
        String searchText = textField.getText().trim();

        // Thực hiện xử lý tìm kiếm dựa trên giá trị đã lấy
        switch (selectedValue) {
            case "Member":
                List<UserModel> userModelList = userDao.getUsersByName(searchText);
                DataHolder.getInstance().setDataList(userModelList);
                DataHolder.getInstance().setData(searchText);
                switchToScene("result_search.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                break;
            case "Post":
                break;
            case "Tag":
                break;
            default:
                break;
        }
        // switchToScene("result_search.fxml", 1300, 750, false);
    }

    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleProfile() {
        switchToScene("profile.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, true);
    }


}
