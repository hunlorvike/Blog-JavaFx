package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.category.CategoryDaoImpl;
import hung.pj.login.dao.category.ICategoryDao;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.CategoryModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import hung.pj.login.ultis.ImageFileUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class AddPostController implements Initializable {
    @FXML
    private HBox hboxContainerImage;
    @FXML
    private Button butttonChooseFiles;
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
    private List<File> selectedFilesList = new ArrayList<>();
    private final IPostDao postDao;
    private final ICategoryDao categoryDao;
    private final UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;

    public AddPostController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        postDao = new PostDaoImpl(connectionProvider.getConnection());
        categoryDao = new CategoryDaoImpl(connectionProvider.getConnection());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Xử lý sự kiện thay đổi giá trị trong ChoiceBox
        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            datePicker.setVisible("Scheduled".equals(newValue));
        });
        List<CategoryModel> categoryModels = categoryDao.getAllCategory();

        // Tạo một danh sách tên danh mục từ danh sách categoryModels
        List<String> categoryNames = new ArrayList<>();
        for (CategoryModel categoryModel : categoryModels) {
            categoryNames.add(categoryModel.getName());
        }

        // Đặt danh sách tên danh mục làm dữ liệu cho ChoiceBox
        categoryChoiceBox.setItems(FXCollections.observableArrayList(categoryNames));

        // Đặt giá trị mặc định nếu cần
        categoryChoiceBox.setValue(categoryNames.get(0)); // Đặt giá trị mặc định là tên danh mục đầu tiên

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

    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Posts");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(butttonChooseFiles.getScene().getWindow());

        if (selectedFiles != null) {
            selectedFilesList.addAll(selectedFiles); // Thêm các tệp đã chọn vào danh sách có thể sửa đổi

            for (File file : selectedFiles) {
                // Kiểm tra nếu tệp là hình ảnh (có thể kiểm tra phần mở rộng hoặc nội dung thực sự)
                if (ImageFileUtil.isImageFile(file)) {
                    HBox imageBox = new HBox(); // Tạo HBox để chứa ImageView và Button

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(75); // Đặt chiều cao của ImageView
                    imageView.setFitWidth(75); // Đặt chiều rộng của ImageView
                    imageView.setPreserveRatio(true); // Duy trì tỉ lệ của ảnh
                    imageView.setImage(new Image(file.toURI().toString()));

                    Button deleteButton = new Button("×"); // Tạo nút X
                    deleteButton.getStyleClass().add("btn-danger"); // Thêm lớp CSS tùy chỉnh


                    deleteButton.setOnAction(event -> {
                        // Xóa ImageView và Button tương ứng
                        hboxContainerImage.getChildren().remove(imageBox);

                        // Xóa tệp khỏi danh sách đã chọn
                        selectedFilesList.remove(file); // Sử dụng danh sách có thể sửa đổi
                    });

                    // Đặt nút X ở góc phải trên cùng của ImageView
                    StackPane stackPane = new StackPane(imageView, deleteButton);
                    StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);

                    // Thêm StackPane vào HBox chứa tất cả
                    imageBox.getChildren().add(stackPane);

                    // Thêm HBox vào HBox chính
                    hboxContainerImage.getChildren().add(imageBox);
                }
            }
        }
    }

}
