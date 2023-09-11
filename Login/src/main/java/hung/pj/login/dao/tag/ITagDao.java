package hung.pj.login.dao.tag;

import hung.pj.login.model.TagModel;

import java.util.List;

public interface ITagDao {
    void addTag(List<TagModel> tagModels);

    List<TagModel> getAllTag();
    List<TagModel> getTagsByName(String tagName);


    void updateTag(TagModel tagModel);

    void deleteTag(int tagId);
}
