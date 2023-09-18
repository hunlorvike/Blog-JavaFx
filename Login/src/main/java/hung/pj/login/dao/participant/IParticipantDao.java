package hung.pj.login.dao.participant;

import hung.pj.login.model.ConversationModel;
import hung.pj.login.model.ParticipantModel;
import hung.pj.login.model.SocialModel;
import hung.pj.login.model.UserModel;

import java.util.List;

public interface IParticipantDao {
    void addParticipant(int userId, int conversationId);    // Thêm một người tham gia mới vào cuộc trò chuyện
    void removeParticipant(int userId, int conversationId);    // Xóa một người tham gia khỏi cuộc trò chuyện
    List<ParticipantModel> getParticipantsByConversation(int conversationId);    // Lấy danh sách người tham gia trong một cuộc trò chuyện
    boolean isUserInConversation(int userId, int conversationId);    // Kiểm tra xem một người dùng đã tham gia vào cuộc trò chuyện hay chưa
//    void addParticipantsToConversation(List<UserModel> userModels, int conversationId);

}

