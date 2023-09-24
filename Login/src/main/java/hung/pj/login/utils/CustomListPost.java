package hung.pj.login.utils;

import hung.pj.login.controller.ItemPostController;
import hung.pj.login.model.PostModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListPost extends ListCell<PostModel> {

    @Override
    protected void updateItem(PostModel postModel, boolean empty) {
        super.updateItem(postModel, empty);

        if(empty || postModel == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hung/pj/login/view/item_post.fxml"));
            try {
                Node node = loader.load();
                ItemPostController controller = loader.getController();
                controller.setListData(postModel); // Thiết lập dữ liệu sách vào giao diện
                setGraphic(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
