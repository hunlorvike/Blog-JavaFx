package hung.pj.login.dao.message;

import hung.pj.login.model.MessageModel;

public interface IMessageDao {
    void sendMessage(MessageModel messageModel);    // Gửi một tin nhắn mới trong một cuộc trò chuyện

}
