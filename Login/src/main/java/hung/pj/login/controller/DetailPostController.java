package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DetailPostController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ImageView UserImageView;
    @FXML
    private Button ActionButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label postContentLabel;
    @FXML
    private ListView imageListView;
    @FXML
    private Label NameLabel;
    @FXML
    private Label CreateLabel;
    @FXML
    private Label ViewLabel;

    private int postId = -1;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String postIdString = DataHolder.getInstance().getData();
        if (postIdString != null && !postIdString.isEmpty()) {
            try {
                postId = Integer.parseInt(postIdString);
                if (postId >= 0) {
                    loadPostData(postId);
                }
            } catch (NumberFormatException | SQLException e) {
                postId = -1;
            }
        }
    }

    private void loadPostData(int postId) throws SQLException {
        UserModel user = postDao.getCreator(postId);
        PostModel post = postDao.getPost(postId);
        List<PostImageModel> imagePost = postDao.getImagePosts(postId);

        //imagePost
        ObservableList<Image> imagesList = FXCollections.observableArrayList();
        if (imagePost != null && !imagePost.isEmpty()) {
            for (PostImageModel postImage : imagePost) {
                String imagePath = postImage.getImagePath();
                Image image = new Image("file:" + imagePath);
                imagesList.add(image);
            }
        }
        if (!imagesList.isEmpty()) {
            System.out.println("Số lượng hình ảnh đã lấy: " + imagesList.size());
        } else {
            System.out.println("Không có hình ảnh nào được lấy từ cơ sở dữ liệu.");
        }
        imageListView.setItems(imagesList);
        imageListView.setCellFactory(listView -> new ListCell<Image>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });
        // post
        if (post != null) {
            String title = post.getTitle();
            String content = post.getContent();
            int viewCount = post.getView_count();
            Timestamp createdAt = post.getCreated_at();

            // Content
            if (content != null && !content.isEmpty()) {
                postContentLabel.setText(content);
            }

            // Title
            if (title != null && !title.isEmpty()) {
                titleLabel.setText(title);
            }

            // viewCount
            ViewLabel.setText("View: " + Integer.toString(viewCount));
            // createdAt
            if (createdAt != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String formattedDate = dateFormat.format(createdAt);
                CreateLabel.setText(formattedDate);
            }else{
                CreateLabel.setText("Lỗi ngày");
            }
        }

        // User
        if (user != null) {
            String fullname = user.getFullname();
            String avatarPath = user.getAvatarPath();

            if (avatarPath != null) {
                File file = new File(avatarPath);
                if (file.exists()) {
                    Image avatarImage = new Image(file.toURI().toString());
                    UserImageView.setImage(avatarImage);
                } else {
                    UserImageView.setImage(null);
                }
            } else {
                UserImageView.setImage(null);
            }
            if (fullname != null && !fullname.isEmpty()) {
                NameLabel.setText(fullname);
            } else {
                NameLabel.setText("Không có thông tin người dùng.");
            }
        } else {
            NameLabel.setText("Không có thông tin người dùng.");
            UserImageView.setImage(null);
        }
    }

    // ButtonAction
    @FXML
    private void ButtonAction() {
        switchToScene("post.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
    }

    private void switchToScene(String fxmlFileName, int width, int height, Boolean useSplash) {
        try {
            AppMain.setRoot(fxmlFileName, width, height, useSplash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
