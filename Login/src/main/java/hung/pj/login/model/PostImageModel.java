package hung.pj.login.model;

import java.sql.Timestamp;

public class PostImageModel {
    private int imageId;
    private int postId;
    private String imagePath;
    private Timestamp createdAt;

    public PostImageModel(int postId, String imagePath) {
        this.postId = postId;
        this.imagePath = imagePath;
    }

    public PostImageModel(int imageId, int postId, String imagePath) {
        this.imageId = imageId;
        this.postId = postId;
        this.imagePath = imagePath;
    }

    public PostImageModel(int imageId, int postId, String imagePath, Timestamp createdAt) {
        this.imageId = imageId;
        this.postId = postId;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }



    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostImageModel{" +
                "imageId=" + imageId +
                ", postId=" + postId +
                ", imagePath='" + imagePath + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
