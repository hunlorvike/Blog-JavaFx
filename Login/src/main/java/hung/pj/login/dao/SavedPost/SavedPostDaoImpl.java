package hung.pj.login.dao.SavedPost;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.SavedPostModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavedPostDaoImpl implements SavedPostDao{
    private final Connection connection;
    public SavedPostDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<SavedPostModel> getAllSavedPosts() {
        List<SavedPostModel> savedPosts = new ArrayList<>();
        String query = "SELECT saved_posts.saved_post_id, post.title AS post_title, users.fullname FROM saved_posts INNER JOIN post ON saved_posts.post_id = post.post_id INNER JOIN users ON saved_posts.user_id = users.user_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int saved_post_id = resultSet.getInt("saved_post_id");
                String postTitle = resultSet.getString("post_title");
                String creatorUsername = resultSet.getString("fullname");

                SavedPostModel savedPostModel = new SavedPostModel(saved_post_id, postTitle, creatorUsername);
                savedPosts.add(savedPostModel);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi truy vấn bài viết đã lưu từ cơ sở dữ liệu.", e);
        }

        return savedPosts;
    }



}
