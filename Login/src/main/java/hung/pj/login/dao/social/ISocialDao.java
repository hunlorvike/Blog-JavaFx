package hung.pj.login.dao.social;

import hung.pj.login.model.SocialModel;

import java.util.List;

public interface ISocialDao {
    void addSocialMedia(List<SocialModel> socialModels);
    SocialModel getSocialMediaByPlatform(int userId, String platform);
    String getProfileUrlByIdAndPlatform(int userId, String platform);
    List<SocialModel> getSocialMediaByUserId(int userId);
    void updateSocialMedia(int userId, SocialModel socialMedia);
    void deleteSocialMedia(int socialModelId);

}
