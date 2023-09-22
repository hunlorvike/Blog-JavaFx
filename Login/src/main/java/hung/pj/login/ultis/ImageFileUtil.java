package hung.pj.login.ultis;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.File;

public class ImageFileUtil {

    public static boolean isImageFile(File file) {
        String extension = getFileExtension(file.getName()).toLowerCase();
        // Kiểm tra các phần mở rộng của tệp ảnh được chấp nhận
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")
                || extension.equals("bmp") || extension.equals("gif")
                || extension.equals("tiff") || extension.equals("tif")
                || extension.equals("ico") || extension.equals("webp")
                || extension.equals("svg") || extension.equals("jp2")
                || extension.equals("pcx") || extension.equals("ppm")
                || extension.equals("pgm") || extension.equals("pnm")
                || extension.equals("exif");
    }

    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex >= 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    public static HBox createImageBox(File file) {
        HBox imageBox = new HBox();
        imageBox.setSpacing(5);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(file.toURI().toString()));

        Button deleteButton = new Button("×");
        deleteButton.getStyleClass().add("btn-danger");

        deleteButton.setOnAction(event -> {
            // Xử lý sự kiện xóa tệp và UI tương ứng
        });

        StackPane stackPane = new StackPane(imageView, deleteButton);
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);

        imageBox.getChildren().add(stackPane);

        return imageBox;
    }
}

