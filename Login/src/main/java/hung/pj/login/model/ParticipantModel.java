package hung.pj.login.model;

public class ParticipantModel {
    private int participantId;
    private int userId;
    private int conversationId;

    public ParticipantModel(int userId, int conversationId) {
        this.userId = userId;
        this.conversationId = conversationId;
    }

    public ParticipantModel(int participantId, int userId, int conversationId) {
        this.participantId = participantId;
        this.userId = userId;
        this.conversationId = conversationId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }
}
