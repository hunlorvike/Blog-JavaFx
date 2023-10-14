package hung.pj.login.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserModel {
    private int user_id;
    private String fullname;
    private String email;
    private String password;
    private String role;
    private LocalDateTime lockedUntil;
    private int postCount;
    private int followCount;
    private String avatarPath;
    private int followersCount;
    private Timestamp created_at;
    private Timestamp updated_at;

    public UserModel() {

    }

    public UserModel(int id, String fullName, int postCount) {
        this.user_id = id;
        this.fullname = fullName;
        this.postCount = postCount;
    }

    public UserModel(int id, String fullName, String email, int postCount) {
        this.user_id = id;
        this.fullname = fullName;
        this.email = email;
        this.postCount = postCount;
    }

    public UserModel(String fullname, String email, String password, String role) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserModel(int user_id, String fullname, String email, String password, String role, Timestamp created_at, Timestamp updated_at) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UserModel(int user_id, String fullname, String email, String password, String role, LocalDateTime lockedUntil, Timestamp created_at, Timestamp updated_at) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.lockedUntil = lockedUntil;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UserModel(int id, String fullname, String email, String password, String role, String avatarPath, int followersCount, Timestamp createdAt, Timestamp updatedAt) {
        this.user_id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatarPath = avatarPath;
        this.followersCount = followersCount;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
    }

    public UserModel(String fullname, String avatarPath) {
        this.fullname = fullname;
        this.avatarPath = avatarPath;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
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
        return "UserModel{" +
                "user_id=" + user_id +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", lockedUntil=" + lockedUntil +
                ", postCount=" + postCount +
                ", followCount=" + followCount +
                ", avatarPath='" + avatarPath + '\'' +
                ", followersCount=" + followersCount +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
