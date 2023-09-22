package hung.pj.login.dao.postImage;

import hung.pj.login.model.PostImageModel;

import java.util.List;

public interface IPostImageDao {
    boolean addPostImages(int postId, List<PostImageModel> postImageModelList);
    List<PostImageModel> getAllImageByIdPost(int postId);
    PostImageModel getImageById(int imageId);
    boolean updatePostImages(int postId, List<PostImageModel> postImageModelList);
    boolean removeAllImageByPostId(int postId);
    boolean removePostImagesById(int imageId);
}
