package hung.pj.login.model;

import java.sql.Timestamp;

public class CategoryModel {
    private int categoryId;
    private String name;
    private int creator_id;
    private Timestamp created_at;
    private Timestamp updated_at;

    public CategoryModel(String name) {
        this.name = name;
    }

    public CategoryModel(String name, int creatorId) {
        this.name = name;
        this.creator_id = creatorId;
    }


    public CategoryModel(int categoryId, String name, int creator_id, Timestamp created_at, Timestamp updated_at) {
        this.categoryId = categoryId;
        this.name = name;
        this.creator_id = creator_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", creator_id=" + creator_id +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
