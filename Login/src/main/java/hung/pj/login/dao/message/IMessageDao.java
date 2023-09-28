package hung.pj.login.dao.message;

import hung.pj.login.model.MessageModel;

import java.util.List;

public interface IMessageDao {
    void sendMessage(MessageModel messageModel);    // Gửi một tin nhắn mới trong một cuộc trò chuyện
    List<MessageModel> getAllMessageByRoomId(int roomId);
}
