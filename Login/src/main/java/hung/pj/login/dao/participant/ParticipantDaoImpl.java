package hung.pj.login.dao.participant;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.ParticipantModel;
import hung.pj.login.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDaoImpl implements IParticipantDao {
    private Connection connection;

    // Khởi tạo đối tượng DAO với kết nối cơ sở dữ liệu
    public ParticipantDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addParticipant(int userId, int conversationId) {
        String query = "INSERT INTO participants (user_id, conversation_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, conversationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while adding participant to the conversation.", e);
        }
    }

    @Override
    public void removeParticipant(int userId, int conversationId) {
        String query = "DELETE FROM participants WHERE user_id = ? AND conversation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, conversationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while removing participant from the conversation.", e);
        }
    }

    @Override
    public List<ParticipantModel> getParticipantsByConversation(int conversationId) {
        List<ParticipantModel> participants = new ArrayList<>();
        String query = "SELECT * FROM participants WHERE conversation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, conversationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    participants.add(new ParticipantModel(userId, conversationId));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching participants by conversation ID.", e);
        }
        return participants;
    }

    @Override
    public boolean isUserInConversation(int userId, int conversationId) {
        String query = "SELECT COUNT(*) FROM participants WHERE user_id = ? AND conversation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, conversationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while checking if user is in conversation.", e);
        }
        return false;
    }

//    @Override
//    public void addParticipantsToConversation(List<UserModel> userModels, int conversationId) {
//        String query = "INSERT INTO participants (user_id, conversation_id) VALUES (?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            for (UserModel userModel : userModels) {
//                statement.setInt(1, userModel.getUser_id());
//                statement.setInt(2, conversationId);
//                statement.addBatch(); // Thêm câu lệnh INSERT vào lô (batch)
//            }
//            statement.executeBatch(); // Thực thi tất cả các câu lệnh INSERT trong lô
//        } catch (SQLException e) {
//            throw new DatabaseException("Error while adding participants to the conversation.", e);
//        }
//    }

}

