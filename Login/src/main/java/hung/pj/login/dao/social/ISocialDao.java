package hung.pj.login.dao.social;

import hung.pj.login.model.SocialModel;

import java.util.List;

public interface ISocialDao {
    void addSocialMedia(SocialModel socialModel);
    SocialModel getSocialMediaByPlatform(int userId, String platform);
    List<SocialModel> getSocialMediaByUserId(int userId);
    void updateSocialMedia(int userId, SocialModel socialMedia);
    void deleteSocialMedia(int socialModelId);

}
