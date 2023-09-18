package hung.pj.login.dao.conversation;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.ConversationModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationDaoImpl implements IConversationDao {
    private Connection connection;

    public ConversationDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createConversation(ConversationModel conversationModel) {
        String query = "INSERT INTO conversations (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, conversationModel.getConversationName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting conversations into the database.", e);
        }
    }

    @Override
    public int getConversationIdByName(String conversationName) throws SQLException {
        String query = "SELECT conversation_id FROM conversations WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, conversationName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("conversation_id");
                }
            }
        }

        return 0;
    }

    @Override
    public int getExistingConversationId(int userAId, int userBId) {
        String query = "SELECT participants1.conversation_id " +
                "FROM participants participants1 " +
                "INNER JOIN participants participants2 " +
                "ON participants1.conversation_id = participants2.conversation_id " +
                "WHERE participants1.user_id = ? " +
                "AND participants2.user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userAId);
            preparedStatement.setInt(2, userBId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while checking conversation existence between users.", e);
        }

        return -1; // Trả về -1 nếu cuộc trò chuyện không tồn tại
    }

    public List<ConversationModel> getConversationsByUserId(int userId) {
        List<ConversationModel> conversations = new ArrayList<>();

        String query = "SELECT c.* " +
                "FROM conversations c " +
                "INNER JOIN participants p " +
                "ON c.conversation_id = p.conversation_id " +
                "WHERE p.user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int conversationId = resultSet.getInt("conversation_id");
                    String name = resultSet.getString("name");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    ConversationModel conversation = new ConversationModel(conversationId, name, createdAt);
                    conversations.add(conversation);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching conversations by user ID", e);
        }

        return conversations;
    }



    @Override
    public ConversationModel getConversationById(int conversationId) {
        String query = "SELECT * FROM conversations WHERE conversation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, conversationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("conversation_id");
                    String name = resultSet.getString("name");
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    ConversationModel conversation = new ConversationModel(id, name, createdAt);
                    return conversation;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching conversations by ID", e);
        }

        return null;
    }

    @Override
    public boolean doesConversationExistBetweenUsers(int userAId, int userBId) {
        String query = "SELECT COUNT(*) " +
                "FROM participants participants1 " +
                "INNER JOIN participants participants2 " +
                "ON participants1.conversation_id = participants2.conversation_id " +
                "WHERE participants1.user_id = ? " +
                "AND participants2.user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userAId);
            preparedStatement.setInt(2, userBId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while checking conversation existence between users.", e);
        }

        return false;
    }


}
