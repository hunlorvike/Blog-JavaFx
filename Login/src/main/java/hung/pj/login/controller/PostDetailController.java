package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.postImage.IPostImageDao;
import hung.pj.login.dao.postImage.PostImageDaoImpl;
import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.ultis.ControllerUtils;
import hung.pj.login.ultis.ImageFileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PostDetailController implements Initializable {
    @FXML
    private Label titleLabel, dateLabel, viewLabel, authorLabel;
    @FXML
    private Text contentText;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());
    IPostImageDao postImageDao = new PostImageDaoImpl(connectionProvider.getConnection());
    private int postId;
    private List<PostImageModel> postImageModels = new ArrayList<>(); // Initialize with current image paths
    private List<PostImageModel> postImageModelsFromDb;
    private final ObservableList<File> selectedFilesList = FXCollections.observableArrayList();

    @FXML
    private HBox hboxContainerImage;

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

    public void loadPostData(int postId) throws SQLException {
        UserModel userModel = postDao.getCreator(postId);
        PostModel postModel = postDao.getPostById(postId);
        if (postModel != null) {
            titleLabel.setText(ControllerUtils.toTitleCase(postModel.getTitle()));
            dateLabel.setText(ControllerUtils.formatDateTime(postModel.getScheduledDate()));
            viewLabel.setText(postModel.getView_count() + " lượt xem");
            authorLabel.setText(ControllerUtils.toAuthorName(userModel.getFullname()));
            contentText.setText(ControllerUtils.capitalizeFirstLetterOnly(postModel.getContent()));
        }
        // Phần ảnh
        postImageModelsFromDb = postImageDao.getAllImageByIdPost(postId); // Gán giá trị cho biến từ CSDL

        for (PostImageModel postImageModel : postImageModelsFromDb) {
            File imageFile = new File(postImageModel.getImagePath());

            if (ImageFileUtil.isImageFile(imageFile)) {
                ImageView imageView = new ImageView(new Image(imageFile.toURI().toString()));
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                hboxContainerImage.getChildren().add(imageView);

                selectedFilesList.add(imageFile);
            }
        }
    }
}
