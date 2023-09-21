package hung.pj.login.dao.post;

import hung.pj.login.model.PostModel;

import java.sql.Timestamp;
import java.util.List;

public interface IPostDao {
    List<PostModel> getAllPosts();

    List<PostModel> getPostsByTag(String tag);

    List<PostModel> getAllPostsByUserId(int userId);

    List<PostModel> getPostsByStatus(String status);

    PostModel getPostById(int post_id);

    boolean insertPost(PostModel postModel);

    boolean deletePost(int post_id);

    boolean updatePost(int post_id, PostModel existingPost);

    boolean updatePostStatusToPublic(int post_id, Timestamp currentTimestamp);

}
