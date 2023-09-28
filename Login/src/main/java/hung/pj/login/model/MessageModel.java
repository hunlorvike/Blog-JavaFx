package hung.pj.login.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageModel implements Serializable {
    private int messageId;
    private int conversationId;
    private int senderId;
    private String messageText;
    private Timestamp sentAt;

    public MessageModel(int conversationId, int senderId, String messageText) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.messageText = messageText;
    }

    public MessageModel(int messageId, int conversationId, int senderId, String messageText, Timestamp sentAt) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentAt = sentAt;
    }


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", senderId=" + senderId +
                ", messageText='" + messageText + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
