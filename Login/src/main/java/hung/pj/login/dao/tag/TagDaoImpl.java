package hung.pj.login.dao.tag;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.TagModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl implements  ITagDao {
    private Connection connection;

    public TagDaoImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void addTag(List<TagModel> tagModels) {
        String query = "INSERT INTO tags (name, created_at, updated_at) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (TagModel tag : tagModels) {
                statement.setString(1, tag.getName());
                statement.setTimestamp(2, tag.getCreated_at());
                statement.setTimestamp(3, tag.getUpdated_at());
                statement.addBatch(); // Thêm câu lệnh INSERT vào lô (batch)
            }
            statement.executeBatch(); // Thực thi tất cả các câu lệnh INSERT trong lô
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting tags into the database.", e);
        }
    }

    @Override
    public List<TagModel> getAllTag() {
        List<TagModel> tags = new ArrayList<>();
        String query = "SELECT * FROM tags"; // Câu lệnh SQL SELECT để lấy tất cả các bản ghi từ bảng tags

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int tagId = resultSet.getInt("tag_id");
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Timestamp updatedAt = resultSet.getTimestamp("updated_at");

                TagModel tag = new TagModel();
                tag.setTag_id(tagId);
                tag.setName(name);
                tag.setCreated_at(createdAt);
                tag.setUpdated_at(updatedAt);

                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching tags from the database.", e);
        }

        return tags;
    }

    @Override
    public List<TagModel> getTagsByName(String tagName) {
        List<TagModel> tags = new ArrayList<>();
        String query = "SELECT * FROM tags WHERE name = ?"; // Câu lệnh SQL SELECT với điều kiện WHERE

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tagName); // Thiết lập giá trị tham số cho tagName

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int tagId = resultSet.getInt("tag_id");
                    String name = resultSet.getString("name");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    Timestamp updatedAt = resultSet.getTimestamp("updated_at");

                    TagModel tag = new TagModel();
                    tag.setTag_id(tagId);
                    tag.setName(name);
                    tag.setCreated_at(createdAt);
                    tag.setUpdated_at(updatedAt);

                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching tags by name from the database.", e);
        }

        return tags;
    }

    @Override
    public void updateTag(TagModel tagModel) {
        String query = "UPDATE tags SET name = ?, created_at = ?, updated_at = ? WHERE tag_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tagModel.getName());
            statement.setTimestamp(2, tagModel.getCreated_at());
            statement.setTimestamp(3, tagModel.getUpdated_at());
            statement.setInt(4, tagModel.getTag_id());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Tag not found with tag_id: " + tagModel.getTag_id());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating tag record in the database.", e);
        }
    }
    @Override
    public void deleteTag(int tagId) {
        String query = "DELETE FROM tags WHERE tag_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, tagId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Tag not found with tag_id: " + tagId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting tag record from the database.", e);
        }
    }

}
