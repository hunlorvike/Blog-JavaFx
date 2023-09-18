package hung.pj.login.dao.post;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.PostModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements IPostDao {
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
                String category = resultSet.getString("category");
                int view_count = resultSet.getInt("view_count");
                int creator_id = resultSet.getInt("creator_id");
                Timestamp scheduledTime = resultSet.getTimestamp("scheduled_datetime");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                PostModel postModel = new PostModel(id, title, content, status, view_count, creator_id, scheduledTime, category, created_at, updated_at);
                posts.add(postModel);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching posts from the database.", e);
        }

        return posts; // Return null if no user with the given ID is found
    }

//    @Override
//    public List<PostModel> getAllPosts() {
//        List<PostModel> posts = new ArrayList<>();
//        String query = "SELECT p.post_id, p.title, p.content, p.status, p.view_count, u.fullname AS user_name, p.scheduled_datetime, p.category, p.created_at, p.updated_at " +
//                "FROM post p " +
//                "INNER JOIN users u ON p.creator_id = u.user_id";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//             var resultSet = preparedStatement.executeQuery()) {
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("post_id");
//                String title = resultSet.getString("title");
//                String content = resultSet.getString("content");
//                String status = resultSet.getString("status");
//                String category = resultSet.getString("category");
//                int view_count = resultSet.getInt("view_count");
//                String userName = resultSet.getString("user_name"); // Sử dụng user_name thay vì creator_id
//                Timestamp scheduledTime = resultSet.getTimestamp("scheduled_datetime");
//                Timestamp created_at = resultSet.getTimestamp("created_at");
//                Timestamp updated_at = resultSet.getTimestamp("updated_at");
//                PostModel postModel = new PostModel(id, title, content, status, view_count, userName, scheduledTime, category, created_at, updated_at);
//                posts.add(postModel);
//            }
//
//        } catch (SQLException e) {
//            throw new DatabaseException("Error while fetching posts from the database.", e);
//        }
//
//        return posts; // Return null if no user with the given ID is found
//    }


    @Override
    public List<PostModel> getPostsByTag(String tag) {
        List<PostModel> posts = new ArrayList<>();
        String query = "SELECT * FROM post WHERE tag = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tag);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("post_id");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    int view_count = resultSet.getInt("view_count");
                    int creator_id = resultSet.getInt("creator_id");
                    Timestamp scheduledTime = resultSet.getTimestamp("scheduled_datetime");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    PostModel postModel = new PostModel(id, title, content, status, view_count, creator_id, scheduledTime, created_at, updated_at);
                    posts.add(postModel);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user by tag", e);
        }

        return posts;
    }

    @Override
    public List<PostModel> getPostsByStatus(String statusInput) {
        List<PostModel> posts = new ArrayList<>();
        String query = "SELECT * FROM post WHERE status = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, statusInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("post_id");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    int view_count = resultSet.getInt("view_count");
                    int creator_id = resultSet.getInt("creator_id");
                    Timestamp scheduledTime = resultSet.getTimestamp("scheduled_datetime");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    PostModel postModel = new PostModel(id, title, content, status, view_count, creator_id, scheduledTime, created_at, updated_at);
                    posts.add(postModel);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user by tag", e);
        }

        return posts;
    }

    @Override
    public boolean insertPost(PostModel postModel) {
        String query = "INSERT INTO post (title, content, status, scheduled_datetime, creator_id, category) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, postModel.getTitle());
            preparedStatement.setString(2, postModel.getContent());
            preparedStatement.setString(3, postModel.getStatus());
            preparedStatement.setTimestamp(4, postModel.getScheduledDate());
            preparedStatement.setInt(5, postModel.getCreator_id());
            preparedStatement.setString(6, postModel.getCategory());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePost(int post_id) {
        String query = "DELETE FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public PostModel getPostById(int post_id) {
        String query = "SELECT * FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, post_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    String category = resultSet.getString("category");
                    return new PostModel(title, content, status, category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updatePost(int id, PostModel postModel) {
        String query = "UPDATE post SET title = ?, content = ?, status = ?, category = ?, scheduled_datetime = ? WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, postModel.getTitle());
            preparedStatement.setString(2, postModel.getContent());
            preparedStatement.setString(3, postModel.getStatus());
            preparedStatement.setString(4, postModel.getCategory());
            preparedStatement.setTimestamp(5, postModel.getScheduledDate());
            preparedStatement.setInt(6, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePostStatusToPublic(int post_id, Timestamp currentTimestamp) {
        String sql = "UPDATE post SET status = 'Published' WHERE post_id = ? AND scheduled_datetime <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, post_id);
            preparedStatement.setTimestamp(2, currentTimestamp);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

