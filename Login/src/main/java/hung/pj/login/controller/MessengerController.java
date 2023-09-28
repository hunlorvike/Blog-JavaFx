package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.conversation.ConversationDaoImpl;
import hung.pj.login.dao.message.MessageDaoImpl;
import hung.pj.login.dao.participant.ParticipantDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.ConversationModel;
import hung.pj.login.model.MessageModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.ControllerUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MessengerController implements Initializable {
    @FXML
    private VBox messageVBox;
    @FXML
    private TextField messageTextField;
    private UserSingleton userSingleton;
    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private ConversationDaoImpl conversationDao = new ConversationDaoImpl(connectionProvider.getConnection());
    private ParticipantDaoImpl participantDao = new ParticipantDaoImpl(connectionProvider.getConnection());
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private MessageDaoImpl messageDao = new MessageDaoImpl(connectionProvider.getConnection());
    @FXML
    private ListView<ConversationModel> conversationListView;
    private List<Label> messageLabels = new ArrayList<>();
    private boolean listeningForMessages = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();

        List<ConversationModel> userConversations = conversationDao.getConversationsByUserId(loggedInUser.getUser_id());

        ObservableList<ConversationModel> observableConversations = FXCollections.observableArrayList(userConversations);
        conversationListView.setItems(observableConversations);

        conversationListView.setCellFactory(new Callback<ListView<ConversationModel>, ListCell<ConversationModel>>() {
            @Override
            public ListCell<ConversationModel> call(ListView<ConversationModel> param) {
                return new ListCell<ConversationModel>() {
                    @Override
                    protected void updateItem(ConversationModel item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getConversationName());
                        }
                    }
                };
            }
        });

        // Thêm lắng nghe sự kiện khi một cuộc trò chuyện được chọn
        conversationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                conversationSelected();
            }
        });

        startListeningForMessages();
    }

    private void startListeningForMessages() {
        Thread messageListeningThread = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 8080);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (listeningForMessages) {
                    try {
                        // Đọc tin nhắn từ máy chủ
                        MessageModel receivedMessage = (MessageModel) in.readObject();

                        // Xử lý tin nhắn mới ở đây
                        // Ví dụ: cập nhật giao diện người dùng để hiển thị tin nhắn mới
                        handleReceivedMessage(receivedMessage);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        messageListeningThread.setDaemon(true); // Đảm bảo luồng nghe dừng khi ứng dụng kết thúc
        messageListeningThread.start();
    }

    private void handleReceivedMessage(MessageModel receivedMessage) {
        Platform.runLater(() -> {
            String senderName = getSenderName(receivedMessage.getSenderId());
            // Lấy thời gian hiện tại
            Date currentTime = new Date();

            // Định dạng thời gian thành giờ:phút:giây
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = dateFormat.format(currentTime);

            String formattedMessage = senderName + ": " + receivedMessage.getMessageText() + " - " + formattedTime;

            Label messageLabel = new Label(formattedMessage);
            messageLabel.setStyle("  -fx-text-fill:  #FFFFFF;");
            HBox messageBox = new HBox(messageLabel);

            // Đặt HBox để có độ rộng vừa với nội dung trong Label
            HBox.setHgrow(messageBox, Priority.ALWAYS);

            // Đặt alignment của HBox dựa trên người gửi tin nhắn
            if (receivedMessage.getSenderId() == userSingleton.getLoggedInUser().getUser_id()) {
                messageBox.setAlignment(Pos.CENTER_RIGHT); // Hiển thị bên phải
                messageBox.setStyle("    -fx-background-color: #445D48; \n" +
                        "    -fx-text-fill:  #FFFFFF;\n" +
                        "    -fx-padding: 5px;\n" +
                        "    -fx-border-radius: 5px; \n" +
                        "    -fx-background-radius: 5px; \n" +
                        "   -fx-background-size: cover");
            } else {
                messageBox.setAlignment(Pos.CENTER_LEFT); // Hiển thị bên trái
                messageBox.setStyle("    -fx-background-color: #B4B4B3; \n" +
                        "    -fx-text-fill:  #FFFFFF;\n" +
                        "    -fx-padding: 5px; \n" +
                        "    -fx-border-radius: 5px; \n" +
                        "    -fx-background-radius: 5px; \n" +
                        "   -fx-background-size: cover");
            }

            messageVBox.getChildren().add(messageBox);
        });
    }


    @FXML
    private void conversationSelected() {
        ConversationModel selectedConversation = conversationListView.getSelectionModel().getSelectedItem();
        if (selectedConversation != null) {
            int conversationId = selectedConversation.getConversationId();
            List<MessageModel> messageModels = messageDao.getAllMessageByRoomId(conversationId);

            // Xóa các Label cũ trước khi thêm các Label mới
            messageLabels.clear();
            messageVBox.getChildren().clear();

            // Tạo Label cho mỗi tin nhắn và thêm vào VBox
            for (MessageModel message : messageModels) {
                String senderName = getSenderName(message.getSenderId()); // Lấy tên người gửi
                // Định dạng thời gian từ tin nhắn
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = dateFormat.format(message.getSentAt());

                String formattedMessage = senderName + ": " + message.getMessageText() + " - " + formattedTime;

                Label label = new Label(formattedMessage);
                label.setStyle("  -fx-text-fill:  #FFFFFF;");
                messageLabels.add(label);

                HBox messageBox = new HBox(label);
                messageBox.setPrefWidth(Region.USE_COMPUTED_SIZE); // Đặt độ rộng của messageBox tự động
                HBox.setHgrow(messageBox, Priority.ALWAYS);   // Đặt HBox để có độ rộng vừa với nội dung trong Label

                // Đặt alignment của HBox dựa trên người gửi tin nhắn
                if (message.getSenderId() == userSingleton.getLoggedInUser().getUser_id()) {
                    messageBox.setAlignment(Pos.CENTER_RIGHT); // Hiển thị bên phải
                    messageBox.setStyle("    -fx-background-color: #445D48; \n" +
                            "    -fx-text-fill:  #FFFFFF;\n" +
                            "    -fx-padding: 5px;\n" +
                            "    -fx-border-radius: 5px; \n" +
                            "    -fx-background-radius: 5px; \n" +
                            "   -fx-background-size: cover");
                } else {
                    messageBox.setAlignment(Pos.CENTER_LEFT); // Hiển thị bên trái
                    messageBox.setStyle("    -fx-background-color: #B4B4B3; \n" +
                            "    -fx-text-fill:  #FFFFFF;\n" +
                            "    -fx-padding: 5px; \n" +
                            "    -fx-border-radius: 5px; \n" +
                            "    -fx-background-radius: 5px; \n" +
                            "   -fx-background-size: cover");
                }

                messageVBox.getChildren().add(messageBox);
            }
        }
    }


    private String getSenderName(int senderId) {
        UserModel userModel = userDao.getUserById(senderId);
        return userModel.getFullname();
    }

    @FXML
    public void sendMessage() {
        String messageText = messageTextField.getText().trim();

        // Kiểm tra nếu tin nhắn không trống
        if (!messageText.isEmpty()) {
            // Lấy cuộc trò chuyện hiện tại được chọn
            ConversationModel selectedConversation = conversationListView.getSelectionModel().getSelectedItem();

            // Kiểm tra xem đã chọn cuộc trò chuyện nào chưa
            if (selectedConversation != null) {
                int conversationId = selectedConversation.getConversationId();

                // Tạo một đối tượng MessageModel
                UserModel loggedInUser = userSingleton.getLoggedInUser();
                int senderId = loggedInUser.getUser_id();
                MessageModel messageModel = new MessageModel(conversationId, senderId, messageText);

                try {
                    Socket socket = new Socket("localhost", 8080);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    // Gửi đối tượng tin nhắn đến máy chủ
                    out.writeObject(messageModel);

                    // Nhận phản hồi từ máy chủ (nếu cần)
                    String response = (String) in.readObject();
                    System.out.println("Phản hồi từ máy chủ: " + response);

                    // Đóng kết nối
                    socket.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // Xóa nội dung trong ô nhập tin nhắn
                messageTextField.clear();
            } else {
                // Hiển thị thông báo hoặc xử lý nếu không có cuộc trò chuyện nào được chọn
                System.out.println("Vui lòng chọn một cuộc trò chuyện trước khi gửi tin nhắn.");
            }
        }
    }
}
