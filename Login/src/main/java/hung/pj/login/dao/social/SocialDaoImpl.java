package hung.pj.login.dao.social;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.SocialModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SocialDaoImpl implements ISocialDao {
    private Connection connection;

    public SocialDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addSocialMedia(List<SocialModel> socialModels) {
        String query = "INSERT INTO social_media (user_id, platform, profile_url) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (SocialModel socialModel : socialModels) {
                statement.setInt(1, socialModel.getUserId());
                statement.setString(2, socialModel.getPlatform());
                statement.setString(3, socialModel.getProfileUrl());
                statement.addBatch(); // Thêm câu lệnh INSERT vào lô (batch)
            }
            statement.executeBatch(); // Thực thi tất cả các câu lệnh INSERT trong lô
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting social media records into the database.", e);
        }
    }


    @Override
    public SocialModel getSocialMediaByPlatform(int userId, String platform) {
        String query = "SELECT * FROM social_media WHERE user_id = ? AND platform = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, platform);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int socialMediaId = resultSet.getInt("social_media_id");
                    String profileUrl = resultSet.getString("profile_url");

                    return new SocialModel(socialMediaId, userId, platform, profileUrl);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching social media record by platform and user_id from the database.", e);
        }

        return null; // Trả về null nếu không tìm thấy bản ghi nào cho nền tảng và user_id đã cho
    }


    @Override
    public List<SocialModel> getSocialMediaByUserId(int userId) {
        List<SocialModel> socialModelList = new ArrayList<>();
        String query = "SELECT * FROM social_media WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int socialMediaId = resultSet.getInt("social_media_id");
                    String platform = resultSet.getString("platform");
                    String profileUrl = resultSet.getString("profile_url");
                    SocialModel socialModel = new SocialModel(socialMediaId, userId, platform, profileUrl);
                    socialModelList.add(socialModel);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching social media records for user from the database.", e);
        }
        return socialModelList;
    }

    @Override
    public void updateSocialMedia(int userId, SocialModel socialModel) {
        String query = "UPDATE social_media SET platform = ?, profile_url = ? WHERE social_media_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, socialModel.getPlatform());
            statement.setString(2, socialModel.getProfileUrl());
            statement.setInt(3, socialModel.getSocialMediaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating social media record in the database.", e);
        }
    }

    // Xóa một bản ghi SocialMedia từ cơ sở dữ liệu
    public void deleteSocialMedia(int socialMediaId) {
        String query = "DELETE FROM social_media WHERE social_media_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, socialMediaId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting social media record from the database.", e);
        }
    }
}
