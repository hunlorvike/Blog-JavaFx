package hung.pj.login.model;

public class SocialModel {
    private int socialMediaId;
    private int userId;
    private String platform;
    private String profileUrl;
    public SocialModel() {

    }
    public SocialModel(int userId, String platform, String profileUrl) {
        this.userId = userId;
        this.platform = platform;
        this.profileUrl = profileUrl;
    }

    public SocialModel(int socialMediaId, int userId, String platform, String profileUrl) {
        this.socialMediaId = socialMediaId;
        this.userId = userId;
        this.platform = platform;
        this.profileUrl = profileUrl;
    }

    public int getSocialMediaId() {
        return socialMediaId;
    }

    public void setSocialMediaId(int socialMediaId) {
        this.socialMediaId = socialMediaId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String toString() {
        return "SocialModel{" +
                "socialMediaId=" + socialMediaId +
                ", userId=" + userId +
                ", platform='" + platform + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }
}
