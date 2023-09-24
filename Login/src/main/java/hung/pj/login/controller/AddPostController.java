package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.category.CategoryDaoImpl;
import hung.pj.login.dao.category.ICategoryDao;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.postImage.IPostImageDao;
import hung.pj.login.dao.postImage.PostImageDaoImpl;
import hung.pj.login.model.CategoryModel;
import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import hung.pj.login.utils.ImageFileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {
    @FXML
    private HBox hboxContainerImage;
    @FXML
    private Button buttonChooseFiles;
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
    private final IPostDao postDao;
    private final ICategoryDao categoryDao;
    private final IPostImageDao postImageDao;
    private final UserSingleton userSingleton = UserSingleton.getInstance();
    private UserModel loggedInUser;
    private ObservableList<File> selectedFilesList = FXCollections.observableArrayList();

    public AddPostController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        postDao = new PostDaoImpl(connectionProvider.getConnection());
        categoryDao = new CategoryDaoImpl(connectionProvider.getConnection());
        postImageDao = new PostImageDaoImpl(connectionProvider.getConnection());
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
            // Lấy danh sách bài viết mới nhất của người dùng hiện tại
            List<PostModel> userPosts = postDao.getAllPostsByUserId(user_id);

            // Kiểm tra xem danh sách có bài viết không
            if (!userPosts.isEmpty()) {
                // Sắp xếp danh sách theo thời gian tạo giảm dần (mới nhất đầu danh sách)
                userPosts.sort((post1, post2) -> post2.getCreated_at().compareTo(post1.getCreated_at()));

                // Lấy bài viết mới nhất từ danh sách (bài viết đầu tiên sau khi sắp xếp)
                PostModel newestPost = userPosts.get(0);

                // Lấy postId của bài viết vừa được thêm
                int postId = newestPost.getPost_id();

                List<PostImageModel> postImageModels = new ArrayList<>();
                for (File file : selectedFilesList) {
                    // Tạo đối tượng PostImageModel từ thông tin file
                    String imagePath = Constants.UPLOAD_DIRECTORY + File.separator + file.getName(); // Đường dẫn mới
                    PostImageModel postImageModel = new PostImageModel(postId, imagePath);
                    postImageModels.add(postImageModel);

                    // Sau đó copy file vào thư mục lưu trữ
                    String storageDirectoryPath = Constants.UPLOAD_DIRECTORY;
                    File storageDirectory = new File(storageDirectoryPath);

                    if (!storageDirectory.exists()) {
                        storageDirectory.mkdirs();
                    }

                    String destinationPath = storageDirectoryPath + File.separator + file.getName();
                    File destinationFile = new File(destinationPath);

                    try {
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Xử lý lỗi sao chép tệp (có thể thông báo cho người dùng)
                    }
                }

                // Thêm các hình ảnh liên quan đến bài viết vào bảng post_images
                boolean addImagesSuccess = postImageDao.addPostImages(postId, postImageModels);

                if (addImagesSuccess) {
                    ControllerUtils.showAlertDialog("Tạo bài viết và hình ảnh thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                    AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                } else {
                    ControllerUtils.showAlertDialog("Tạo bài viết thành công nhưng có lỗi khi thêm hình ảnh", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                }
            }

        } else {
            ControllerUtils.showAlertDialog("Tạo bài viết thất bại", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
        }
    }


    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Posts");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(buttonChooseFiles.getScene().getWindow());

        if (selectedFiles != null) {
            selectedFilesList.addAll(selectedFiles);

            for (File file : selectedFiles) {
                // Kiểm tra nếu tệp là hình ảnh (có thể kiểm tra phần mở rộng hoặc nội dung thực sự)
                if (ImageFileUtils.isImageFile(file)) {
                    HBox imageBox = createImageBox(file);
                    hboxContainerImage.getChildren().add(imageBox);
                }
            }
        }
    }

    private HBox createImageBox(File file) {
        HBox imageBox = new HBox();
        imageBox.setSpacing(5);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(file.toURI().toString()));

        Button deleteButton = new Button("×");
        deleteButton.getStyleClass().add("btn-danger");

        deleteButton.setOnAction(event -> {
            hboxContainerImage.getChildren().remove(imageBox);
            selectedFilesList.remove(file);
        });

        StackPane stackPane = new StackPane(imageView, deleteButton);
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);

        imageBox.getChildren().add(stackPane);

        return imageBox;
    }

    private void copySelectedFilesToStorage() {
        String storageDirectoryPath = Constants.UPLOAD_DIRECTORY;
        File storageDirectory = new File(storageDirectoryPath);

        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }

        for (File selectedFile : selectedFilesList) {
            String destinationPath = storageDirectoryPath + File.separator + selectedFile.getName();
            File destinationFile = new File(destinationPath);

            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi sao chép tệp (có thể thông báo cho người dùng)
            }
        }
    }
}
