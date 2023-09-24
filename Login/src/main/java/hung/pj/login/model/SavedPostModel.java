package hung.pj.login.model;

public class SavedPostModel {
    private int saved_post_id;
    private int user_id;
    private int post_id;
    private String postTitle;
    private String creatorUsername;

    public SavedPostModel() {
    }


    public SavedPostModel(int saved_post_id, String postTitle, String creatorUsername) {
        this.saved_post_id = saved_post_id;
        this.postTitle = postTitle;
        this.creatorUsername = creatorUsername;
    }

    public int getSaved_post_id() {
        return saved_post_id;
    }

    public void setSaved_post_id(int saved_post_id) {
        this.saved_post_id = saved_post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle() {this.postTitle = postTitle;}

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername() {this.creatorUsername = creatorUsername;}

}

