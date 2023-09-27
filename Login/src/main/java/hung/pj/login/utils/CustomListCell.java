package hung.pj.login.utils;

import hung.pj.login.controller.ItemSearchController;
import hung.pj.login.model.UserModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListCell<T> extends ListCell<T> {
    public CustomListCell() {
        // Khởi tạo constructor nếu cần
    }

    // Ghi đè phương thức updateItem để hiển thị dữ liệu
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Tạo và hiển thị giao diện từ item_search.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hung/pj/login/view/item_search.fxml"));
            try {
                Node node = loader.load();
                ItemSearchController controller = loader.getController();
                controller.setListData(item); // Thiết lập dữ liệu vào giao diện
                setGraphic(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
