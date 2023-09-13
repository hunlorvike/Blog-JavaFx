package hung.pj.login.model;

import java.sql.Timestamp;

public class Notification {
    public int notificationId;
    public int userid;
    public String content;
    public boolean is_read;
    public Timestamp created_at;

    public Notification(int notificationId, int userid, String content, boolean is_read, Timestamp created_at) {
        this.notificationId = notificationId;
        this.userid = userid;
        this.content = content;
        this.is_read = is_read;
        this.created_at = created_at;
    }

    public Notification() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
