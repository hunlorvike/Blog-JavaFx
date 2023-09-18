package hung.pj.login.dao.conversation;

import hung.pj.login.model.ConversationModel;

import java.sql.SQLException;
import java.util.List;

public interface IConversationDao {
    void createConversation(ConversationModel conversationModel); // Tạo một cuộc trò chuyện mới:
    int getConversationIdByName(String conversationName) throws SQLException;
    int getExistingConversationId(int userAId, int userBId);
    List<ConversationModel> getConversationsByUserId(int userId);
    ConversationModel getConversationById(int conversationId) throws SQLException; //Lấy thông tin cuộc trò chuyện dựa trên conversation_id:
    boolean doesConversationExistBetweenUsers(int userAId, int userBId); // Kiểm tra xem một cuộc trò chuyện đã tồn tại giữa hai người dùng hay chưa:
}
