package hung.pj.login;

import hung.pj.login.ultis.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Objects;

public class AppMain extends Application {
    private static Stage stage;
    private static Scene scene;
    private static final Screen screen = Screen.getPrimary();
    private static final double screenWidth = screen.getBounds().getWidth();
    private static final double screenHeight = screen.getBounds().getHeight();

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/login.fxml"));
        scene = new Scene(fxmlLoader.load(), 1024, 600);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet()); // BootstrapFX
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("image/1.jpg")))); //Icon App
        stage.setTitle(Constants.APP_NAME);
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(600);

        // Tính toán tọa độ để đặt màn hình ở chính giữa
        double newStageX = (screenWidth - 1024) / 2;
        double newStageY = (screenHeight - 600) / 2;

        stage.setX(newStageX);
        stage.setY(newStageY);
        
        stage.show();
    }

    // Phương thức chuyển giữa các giao diện
    public static void setRoot(String fxml, double width, double height, boolean useSplash) throws IOException {
        if (useSplash) {
            // Nếu sử dụng Splash và đang chuyển đến trang Splash, thực hiện chuyển trang sau 5 giây
            Parent splashRoot = loadFXML("splash.fxml", 600, 1024);
            scene.setRoot(splashRoot);

            // Tạo một PauseTransition để chờ 5 giây trước khi chuyển trang
            PauseTransition splashPause = new PauseTransition(Duration.seconds(2));
            splashPause.setOnFinished(event -> {
                try {
                    // Sau khi kết thúc thời gian chờ, chuyển đến trang Dashboard
                    setRoot(fxml, width, height, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            splashPause.play();
        } else {
            // Nếu không sử dụng Splash hoặc chuyền vào fxml là null, chuyển trang ngay lập tức
            Parent root = loadFXML(fxml, width, height);
            scene.setRoot(root);
        }

        if (stage != null) {
            stage.setWidth(width);
            stage.setHeight(height);

            // Tính toán tọa độ để đặt màn hình ở chính giữa
            double newStageX = (screenWidth - width) / 2;
            double newStageY = (screenHeight - height) / 2;

            stage.setX(newStageX);
            stage.setY(newStageY);

            stage.show();
        }
    }

    private static Parent loadFXML(String fxml, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("view/" + fxml));
        Parent root = fxmlLoader.load();

        if (stage != null) {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.show();
        }

        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}
