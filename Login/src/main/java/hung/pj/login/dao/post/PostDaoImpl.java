package hung.pj.login.dao.post;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.PostModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements IPostDao{
    private final Connection connection;

    public PostDaoImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<PostModel> getAllPosts() {
        List<PostModel> posts = new ArrayList<>();
        String query = "SELECT * FROM post";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("post_id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                int view_count = resultSet.getInt("view_count");
                int creator_id = resultSet.getInt("creator_id");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                PostModel postModel = new PostModel(id, title, content, status, view_count,creator_id, created_at, updated_at);
                posts.add(postModel);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching posts from the database.", e);
        }

        return posts;
    }

    @Override
    public int insertPost(PostModel postModel) {
        String query = "INSERT INTO post (title, content, status, creator_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, postModel.getTitle());
            preparedStatement.setString(2, postModel.getContent());
            preparedStatement.setString(3, postModel.getStatus());
            preparedStatement.setInt(4, postModel.getCreator_id());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? 1 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void editPost(int post_id, String title, String content, String status) {
        String query = "UPDATE post SET title = ?, content = ?, status = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while editing post.", e);
        }
    }


    @Override
    public int deletePost(int post_id) {
        String query = "DELETE FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? 1 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public PostModel getPostById(int post_id) {
        String sql = "SELECT title, content, status FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, post_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    return new PostModel(title, content, status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int updatePost(PostModel existingPost) {
        String query = "UPDATE post SET title = ?, content = ?, status = ? WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, existingPost.getTitle());
            preparedStatement.setString(2, existingPost.getContent());
            preparedStatement.setString(3, existingPost.getStatus());
            preparedStatement.setInt(4, existingPost.getPost_id());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? 1 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


}

