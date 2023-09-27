package hung.pj.login.dao.post;

import hung.pj.login.model.PostImageModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;

import java.sql.SQLException;
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

    int getUserIdForPost(int selectedPostId);

    boolean updatePost(int post_id, PostModel existingPost);

    boolean updatePostStatusToPublic(int post_id, Timestamp currentTimestamp);

    boolean insertSavedPost(int user_id, int selectedId);

    int insertPostAndGetId(PostModel postModel);

    boolean insertImage(String imagePath, int postId);

    PostModel getPost(int postId) throws SQLException;

    UserModel getCreator(int postId);

    boolean increaseViewCount(int selectedId);

    List<PostImageModel> getImagePosts(int postId);
}
