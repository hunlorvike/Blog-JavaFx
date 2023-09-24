package hung.pj.login.ultis;

import hung.pj.login.model.SavedPostModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;

import java.util.List;
import java.util.Optional;

public class ControllerUtils {
    public static <T> void refreshTableView(TableView<T> tableView, List<T> dataList) {
        ObservableList<T> data = FXCollections.observableArrayList(dataList);
        tableView.setItems(data);
    }


    public static void showAlertDialog(String content, Alert.AlertType alertType, Window ownerWindow) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(ownerWindow); // Đặt cửa sổ chủ ở đây
        alert.showAndWait();
    }

    public static Optional<String> showTextInputDialog(String title, String headerText, String contentText, Window ownerWindow) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.initOwner(ownerWindow); // Đặt cửa sổ chủ ở đây
        return dialog.showAndWait();
    }
}

