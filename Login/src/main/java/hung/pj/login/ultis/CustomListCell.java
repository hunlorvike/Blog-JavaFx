package hung.pj.login.ultis;

import hung.pj.login.controller.ItemSearchController;
import hung.pj.login.model.UserModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListCell extends ListCell<UserModel> {
    public CustomListCell() {
        // Khởi tạo constructor nếu cần
    }

    // Ghi đè phương thức updateItem để hiển thị dữ liệu sách
    @Override
    protected void updateItem(UserModel userModel, boolean empty) {
        super.updateItem(userModel, empty);

        if (empty || userModel == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Tạo và hiển thị giao diện từ item_book.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hung/pj/login/view/item_search.fxml"));
            try {
                Node node = loader.load();
                ItemSearchController controller = loader.getController();
                controller.setListData(userModel); // Thiết lập dữ liệu sách vào giao diện
                setGraphic(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
