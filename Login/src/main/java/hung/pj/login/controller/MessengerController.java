package hung.pj.login.controller;

import hung.pj.login.singleton.WebSocketClientSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MessengerController {
    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox messengerVBox;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendMessage;

    @FXML
    private void initialize() {
        // Khởi tạo controller (có thể làm các tác vụ khởi đầu ở đây)
    }

    @FXML
    private void handleSendMessage() {
        // Xử lý sự kiện khi người dùng nhấn nút "Send"
        String message = messageTextField.getText();
        if (!message.isEmpty()) {
            // Gửi tin nhắn tới WebSocket Server
            WebSocketClientSingleton.getInstance().sendMessage(message);

            // Hiển thị tin nhắn trên giao diện
            displayMessage("You: " + message);

            // Xóa nội dung trong TextField
            messageTextField.clear();
        }
    }

    // Phương thức để hiển thị tin nhắn trên giao diện
    private void displayMessage(String message) {
        // Tạo một Label hoặc Text và thêm vào messengerVBox
        // Đảm bảo cuộc trò chuyện được cuộn xuống dưới khi có tin nhắn mới
        // Ví dụ: messengerVBox.getChildren().add(new Label(message));
    }
}
