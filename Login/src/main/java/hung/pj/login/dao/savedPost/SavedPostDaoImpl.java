package hung.pj.login.dao.savedPost;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.PostModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavedPostDaoImpl implements SavedPostDao {
    private final Connection connection;

    public SavedPostDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<PostModel> getAllSavedPosts() {
        List<PostModel> savedPosts = new ArrayList<>();
        String query = "SELECT saved_posts.saved_post_id, post.* FROM saved_posts " +
                "INNER JOIN post ON saved_posts.post_id = post.post_id " +
                "INNER JOIN users ON saved_posts.user_id = users.user_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int saved_post_id = resultSet.getInt("saved_post_id");
                int post_id = resultSet.getInt("post_id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                int viewCount = resultSet.getInt("view_count");
                int creator_id = resultSet.getInt("creator_id");
                Timestamp scheduledDatetime = resultSet.getTimestamp("scheduled_datetime");
                String category = resultSet.getString("category");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");

                PostModel postModel = new PostModel(saved_post_id, post_id, title, content, status, viewCount, creator_id, scheduledDatetime, created_at, updated_at);
                savedPosts.add(postModel);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi truy vấn bài viết đã lưu từ cơ sở dữ liệu.", e);
        }

        return savedPosts;
    }

    @Override
    public boolean deleteSavedPost(int saved_post_id) {
        String deleteQuery = "DELETE FROM saved_posts WHERE saved_post_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, saved_post_id);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi xóa bài viết đã lưu từ cơ sở dữ liệu.", e);
        }
    }

}
