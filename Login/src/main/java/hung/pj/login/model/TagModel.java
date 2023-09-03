package hung.pj.login.model;

import java.sql.Timestamp;

public class TagModel {
    private int tag_id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;

    public TagModel() {

    }

    public TagModel(int tag_id, String name, Timestamp created_at, Timestamp updated_at) {
        this.tag_id = tag_id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "TagModel{" +
                "tag_id=" + tag_id +
                ", name='" + name + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
