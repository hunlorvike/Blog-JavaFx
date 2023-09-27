package hung.pj.login.utils;

import hung.pj.login.model.SavedPostModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return ""; // Xử lý khi timestamp là null (tuỳ theo yêu cầu của bạn)
        }
        SimpleDateFormat outputFormatter = new SimpleDateFormat("MMMM d, yyyy");
        return outputFormatter.format(timestamp);
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
            }
        }

        return result.toString();
    }

    public static String toAuthorName(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            return "";
        }

        return "Được đăng bởi " + authorName;
    }

    public static String capitalizeFirstLetterOnly(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}

