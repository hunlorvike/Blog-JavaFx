package hung.pj.login.dao.message;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.MessageModel;
import hung.pj.login.model.PostModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements IMessageDao {
    private Connection connection;

    // Khởi tạo đối tượng DAO với kết nối cơ sở dữ liệu
    public MessageDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void sendMessage(MessageModel messageModel) {
        String query = "INSERT INTO messages (conversation_id, sender_id, message_text) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, messageModel.getConversationId());
            preparedStatement.setInt(2, messageModel.getSenderId());
            preparedStatement.setString(3, messageModel.getMessageText());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while sending message.", e);
        }
    }

    @Override
    public List<MessageModel> getAllMessageByRoomId(int roomId) {
        List<MessageModel> messageModels = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE conversation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, roomId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("message_id");
                    int conversation_id = resultSet.getInt("conversation_id");
                    int sender_id = resultSet.getInt("sender_id");
                    String message_text = resultSet.getString("message_text");
                    Timestamp sent_at = resultSet.getTimestamp("sent_at");
                    MessageModel messageModel = new MessageModel(id, conversation_id, sender_id, message_text, sent_at);
                    messageModels.add(messageModel);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user by id", e);
        }

        return messageModels;
    }
}