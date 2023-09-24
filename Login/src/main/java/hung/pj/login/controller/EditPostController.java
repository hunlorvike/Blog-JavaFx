package hung.pj.login.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.postImage.PostImageDaoImpl;
import hung.pj.login.dao.postImage.IPostImageDao;
import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.Constants;
import hung.pj.login.ultis.ControllerUtils;
import hung.pj.login.ultis.ImageFileUtil;
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

public class EditPostController implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    private Button buttonChooseFiles;
    @FXML
    private HBox hboxContainerImage;
    @FXML
    private TextField titleTextField, linkTextField;
    @FXML
    private TextArea contentTextField;
    @FXML
    private ChoiceBox<String> statusComboBox, categoryChoiceBox;
    @FXML
    private DatePicker datePicker;
    private final IPostDao postDao;
    private final IPostImageDao postImageDao;
    private int postId = -1;
    private final ObservableList<File> selectedFilesList = FXCollections.observableArrayList();
    private List<PostImageModel> postImageModels = new ArrayList<>(); // Initialize with current image paths
    private List<PostImageModel> postImageModelsFromDb;

    // Constants
//    private static final String UPLOAD_DIRECTORY = "Login/src/main/resources/hung/pj/login/upload";

    public EditPostController() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        postDao = new PostDaoImpl(connectionProvider.getConnection());
        postImageDao = new PostImageDaoImpl(connectionProvider.getConnection());
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

    // Load post data
    private void loadPostData(int postId) {
        PostModel selectedPost = postDao.getPostById(postId);
        if (selectedPost != null) {
            titleTextField.setText(selectedPost.getTitle());
            contentTextField.setText(selectedPost.getContent());
            statusComboBox.setValue(selectedPost.getStatus());
            categoryChoiceBox.setValue(selectedPost.getCategory());
        }

        postImageModelsFromDb = postImageDao.getAllImageByIdPost(postId); // Gán giá trị cho biến từ CSDL

        for (PostImageModel postImageModel : postImageModelsFromDb) {
            File imageFile = new File(postImageModel.getImagePath());

            if (ImageFileUtil.isImageFile(imageFile)) {
                HBox imageBox = createImageBox(imageFile);
                hboxContainerImage.getChildren().add(imageBox);

                selectedFilesList.add(imageFile);
            }
        }
    }

    // Handle edit post
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

                for (File file : selectedFilesList) {
                    String imagePath = Constants.UPLOAD_DIRECTORY + "/" + file.getName(); // New path
                    PostImageModel postImageModel = new PostImageModel(postId, imagePath);
                    postImageModels.add(postImageModel);

                    File storageDirectory = new File(Constants.UPLOAD_DIRECTORY);
                    if (!storageDirectory.exists()) {
                        storageDirectory.mkdirs();
                    }

                    String destinationPath = Constants.UPLOAD_DIRECTORY + File.separator + file.getName();
                    File destinationFile = new File(destinationPath);

                    try {
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle file copy error (you can notify the user)
                    }
                }

                boolean isUpdateImage = postImageDao.updatePostImages(postId, postImageModels);

                if (isUpdateImage && updateSuccess) {
                    ControllerUtils.showAlertDialog("Sửa bài viết và ảnh thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
                    AppMain.setRoot("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
                } else {
                    ControllerUtils.showAlertDialog("Sửa bài viết và ảnh thất bại", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
                }
            }
        }
    }

    // Create image box
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

    // Upload files
    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Posts");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(buttonChooseFiles.getScene().getWindow());

        if (selectedFiles != null) {
            selectedFilesList.addAll(selectedFiles);

            for (File file : selectedFiles) {
                if (ImageFileUtil.isImageFile(file)) {
                    HBox imageBox = createImageBox(file);
                    hboxContainerImage.getChildren().add(imageBox);
                }
            }
        }
    }
}
