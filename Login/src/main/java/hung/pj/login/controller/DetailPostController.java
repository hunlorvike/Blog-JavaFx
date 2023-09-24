package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class DetailPostController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextArea conTentTextArea;
    @FXML
    private ImageView UserImageView;
    @FXML
    private ImageView PostImageView;
    @FXML
    private Label titleLabel;
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

        if (post != null) {
            String title = post.getTitle();
            String content = post.getContent();
            int viewCount = post.getView_count();
            Timestamp createdAt = post.getCreated_at();

            // Content
            if (content != null && !content.isEmpty()) {
                conTentTextArea.setText(content);
            }

            // Title
            if (title != null && !title.isEmpty()) {
                titleLabel.setText(title);
            }

            // viewCount
            ViewLabel.setText(Integer.toString(viewCount));
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


}
