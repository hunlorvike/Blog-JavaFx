package hung.pj.login.dao.postImage;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.PostImageModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostImageDaoImpl implements IPostImageDao {
    private Connection connection;

    public PostImageDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addPostImages(int postId, List<PostImageModel> postImageModelList) {
        String query = "INSERT INTO post_images (post_id, image_path) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (PostImageModel postImageModel : postImageModelList) {
                preparedStatement.setInt(1, postId);
                preparedStatement.setString(2, postImageModel.getImagePath());
                preparedStatement.addBatch();
            }
            int[] results = preparedStatement.executeBatch();

            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting post images into the database.", e);
        }
    }

    @Override
    public List<PostImageModel> getAllImageByIdPost(int postId) {
        List<PostImageModel> imageList = new ArrayList<>();
        String query = "SELECT * FROM post_images WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int imageId = resultSet.getInt("image_id");
                    String imagePath = resultSet.getString("image_path");
                    PostImageModel postImageModel = new PostImageModel(imageId, postId, imagePath);
                    imageList.add(postImageModel);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching post images from the database.", e);
        }
        return imageList;
    }

    @Override
    public PostImageModel getImageById(int imageId) {
        String query = "SELECT * FROM post_images WHERE image_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int postId = resultSet.getInt("post_id");
                    String imagePath = resultSet.getString("image_path");
                    return new PostImageModel(imageId, postId, imagePath);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching post image from the database.", e);
        }
        return null;
    }

    @Override
    public boolean updatePostImages(int postId, List<PostImageModel> postImageModelList) {
        removeAllImagesByPostId(postId);
        return addPostImages(postId, postImageModelList);
    }

    @Override
    public boolean removeAllImagesByPostId(int postId) {
        String query = "DELETE FROM post_images WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error while removing post images by post id from the database.", e);
        }
    }

    @Override
    public boolean removePostImagesById(int imageId) {
        String query = "DELETE FROM post_images WHERE image_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error while removing post image by image id from the database.", e);
        }
    }
}
