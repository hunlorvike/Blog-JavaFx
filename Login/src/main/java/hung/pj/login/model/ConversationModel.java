package hung.pj.login.model;

import java.sql.Timestamp;

public class ConversationModel {
    private int conversationId;
    private String conversationName;
    private Timestamp createdAt;

    public ConversationModel() {

    }

    public ConversationModel(String conversationName) {
        this.conversationName = conversationName;
    }

    public ConversationModel(int conversationId, String conversationName, Timestamp createdAt) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.createdAt = createdAt;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ConversationModel{" +
                "conversationId=" + conversationId +
                ", conversationName='" + conversationName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
