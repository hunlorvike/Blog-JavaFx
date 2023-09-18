package hung.pj.login.dao.message;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.MessageModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
