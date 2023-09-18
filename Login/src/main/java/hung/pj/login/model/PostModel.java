package hung.pj.login.model;

import java.sql.Time;
import java.sql.Timestamp;

public class PostModel {
    private int post_id;
    private String title;
    private String content;
    private String status;
    private int view_count;
    private int creator_id;
    private String category;
    private Timestamp scheduledDate;
    private Timestamp created_at;
    private Timestamp updated_at;

    public PostModel(String title, String content, String status, String category) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.category = category;
    }

    public PostModel(String title, String content, String status, Timestamp scheduledDate, String category) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.scheduledDate = scheduledDate;
        this.category = category;
    }

    public PostModel(String title, String content, String status, Timestamp scheduledDate, int creator_id, String category) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.scheduledDate = scheduledDate;
        this.creator_id = creator_id;
        this.category = category;
    }

    public PostModel(int post_id, String title, String content, String status, int view_count, int creator_id, Timestamp scheduledDate, Timestamp created_at, Timestamp updated_at) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.view_count = view_count;
        this.creator_id = creator_id;
        this.scheduledDate = scheduledDate;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public PostModel(int post_id, String title, String content, String status, int view_count, int creator_id, Timestamp scheduledDate, String categoru, Timestamp created_at, Timestamp updated_at) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.view_count = view_count;
        this.creator_id = creator_id;
        this.scheduledDate = scheduledDate;
        this.category = categoru;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }



    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public Timestamp getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Timestamp scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "post_id=" + post_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", view_count=" + view_count +
                ", creator_id=" + creator_id +
                ", scheduledDate=" + scheduledDate +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}

