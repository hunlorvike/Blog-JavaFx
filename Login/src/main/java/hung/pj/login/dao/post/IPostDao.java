package hung.pj.login.dao.post;

import hung.pj.login.model.PostModel;

import java.util.List;

public interface IPostDao{
    List<PostModel> getAllPosts();

    int insertPost(PostModel postModel);

    int deletePost(int post_id);

    void editPost(int post_id, String title, String content, String status);

    PostModel  getPostById(int post_id);

    int updatePost(PostModel existingPost);
}
