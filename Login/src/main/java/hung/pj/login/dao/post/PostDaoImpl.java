package hung.pj.login.dao.post;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;

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
    public List<PostModel> getPostsByName(String name) {
        List<PostModel> posts = new ArrayList<>();
        String query = "SELECT * FROM post WHERE title LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("post_id");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String status = resultSet.getString("status");
                    int view_count = resultSet.getInt("view_count");
                    int creator_id = resultSet.getInt("creator_id");
                    String category = resultSet.getString("category");
                    Timestamp scheduledTime = resultSet.getTimestamp("scheduled_datetime");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    PostModel postModel = new PostModel(id, title, content, status, view_count, creator_id, scheduledTime, category, created_at, updated_at);
                    posts.add(postModel);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user by tag", e);
        }

        return posts;
    }

    @Override
    public List<PostModel> getAllPostsByUserId(int userId) {
        List<PostModel> posts = new ArrayList<>();
        String query = "SELECT * FROM post WHERE creator_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

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
                    return postModel;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getUserIdForPost(int selectedPostId) {
        int userId = -1;
        String query = "SELECT creator_id FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedPostId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("creator_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
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

    @Override
    public boolean insertSavedPost(int user_id, int selectedId) {
        String query = "INSERT INTO saved_posts (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedId);
            preparedStatement.setInt(2, user_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public int insertPostAndGetId(PostModel postModel) {
        String query = "INSERT INTO post (title, content, status, scheduled_datetime, creator_id, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, postModel.getTitle());
            preparedStatement.setString(2, postModel.getContent());
            preparedStatement.setString(3, postModel.getStatus());

            preparedStatement.setTimestamp(4, postModel.getScheduledDate());
            preparedStatement.setInt(5, postModel.getCreator_id());
            preparedStatement.setString(6, postModel.getCategory());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                return -1;
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean insertImage(String imagePath, int postId) {
        String query = "INSERT INTO post_images (post_id, image_path) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            preparedStatement.setString(2, imagePath);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public PostModel getPost(int postId) throws SQLException {
        PostModel post = null;
        String query = "SELECT title, content, view_count, created_at FROM post WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    int view_count = resultSet.getInt("view_count");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    post = new PostModel(title, content, view_count, created_at);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return post;
        }
    }

    @Override
    public UserModel getCreator(int postId) {
        UserModel user = null;
        String query = "SELECT users.fullname, users.avatar_path FROM post JOIN users ON post.creator_id = users.user_id WHERE post.post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String fullname = resultSet.getString("fullname");
                    String avatar = resultSet.getString("avatar_path");
                    user = new UserModel(fullname, avatar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean increaseViewCount(int selectedId) {
        String sql = "UPDATE post SET view_count = view_count + 1 WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, selectedId);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PostImageModel> getImagePosts(int postId) {
        List<PostImageModel> images = new ArrayList<>();
        String query = "SELECT post_images.image_id, post_images.image_path FROM post_images JOIN post ON post_images.post_id  = post.post_id  WHERE post.post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("image_id");
                    String image_path = resultSet.getString("image_path");
                    PostImageModel image = new PostImageModel(id, image_path);
                    images.add(image);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }

}

