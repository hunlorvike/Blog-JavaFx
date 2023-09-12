package hung.pj.login.dao.post;

import hung.pj.login.model.PostModel;

import java.util.List;

public interface IPostDao {
    List<PostModel> getAllPosts();

    List<PostModel> getPostsByTag(String tag);

    List<PostModel> getPostsByStatus(String status);

    PostModel getPostById(int post_id);

    boolean insertPost(PostModel postModel);

    boolean deletePost(int post_id);

    boolean editPost(int post_id, String title, String content, String status);

    boolean updatePost(int post_id, PostModel existingPost);
}
