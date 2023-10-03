package hung.pj.login.dao.savedPost;

import hung.pj.login.model.PostModel;

import java.util.List;

public interface SavedPostDao {
    List<PostModel> getAllSavedPosts();
    boolean deleteSavedPost(int saved_post_id);

}
