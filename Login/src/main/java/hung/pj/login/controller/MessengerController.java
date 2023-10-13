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
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        UserModel loggedInUser = userSingleton.getLoggedInUser();
        userSingleton.setOnlineStatus(true);

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
                            setAlignment(Pos.CENTER);
                        } else {
                            String conversationInfo = "Room: " + item.getConversationName();
                            setText(conversationInfo);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });

        try {
            socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Thêm lắng nghe sự kiện khi một cuộc trò chuyện được chọn
        conversationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                conversationSelected();
            }
        });
        // Chạy phương thức khi mở messenger
        startListeningForMessages();
    }

    // Nhiệm vụ chính của phương thức này là duyệt qua các tin nhắn mới từ máy chủ và cập nhật giao diện người dùng cho các thành viên trong nhóm trò chuyện
    private void startListeningForMessages() {
        // Phương thức này tạo một luồng mới. Luồng này sẽ chịu trách nhiệm lắng nghe và xử lý tin nhắn đến từ máy chủ.
        Thread messageListeningThread = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 8080); // Client tạo một socket để kết nô với máy chủ đang lắng nghe tại cổng 8080
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // Tạo một object từ class ObjectInputStream
                System.out.println(in);
                // listeningForMessages = true. Mục đích của việc này là để máy khách có thể lắng nghe tin nhắn một cách liên tục trong khi ứng dụng vẫn hoạt động.
                while (listeningForMessages) {
                    try {
                        // Đọc tin nhắn vừa gửi đi và câ nhật vào giao diện
                        MessageModel receivedMessage = (MessageModel) in.readObject();
                        System.out.println(receivedMessage);

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

        messageListeningThread.setDaemon(true); // Trong trường hợp này, nó đảm bảo rằng luồng lắng nghe tin nhắn sẽ dừng khi ứng dụng kết thúc.
        messageListeningThread.start(); // Luồng lắng nghe tin nhắn được khởi động để bắt đầu lắng nghe và xử lý tin nhắn từ máy chủ.
    }

    // Được gọi khi người dùng chọn một cuộc trò chuyện từ danh sách cuộc trò chuyện trên giao diện ứng dụng Messenger của bạn.
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


    private String getSenderName(int senderId) {
        UserModel userModel = userDao.getUserById(senderId);
        return userModel.getFullname();
    }

    @FXML
    public void sendMessage() {
        String messageText = messageTextField.getText().trim();

        if (!messageText.isEmpty()) {
            ConversationModel selectedConversation = conversationListView.getSelectionModel().getSelectedItem();

            if (selectedConversation != null) {
                int conversationId = selectedConversation.getConversationId();
                UserModel loggedInUser = userSingleton.getLoggedInUser();
                int senderId = loggedInUser.getUser_id();
                MessageModel messageModel = new MessageModel(conversationId, senderId, messageText); // Tạo một object messagemodel để gửi tới server

                try {
                    // Gửi đối tượng tin nhắn đến máy chủ
                    out.writeObject(messageModel);
                    out.flush(); // Đảm bảo dữ liệu được gửi ngay lập tức thay vì đợi đến khi bộ đệm đầy.

                    // Nhận phản hồi từ máy chủ (nếu cần)
                    String response = (String) in.readObject();
                    System.out.println("Phản hồi từ máy chủ: " + response);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                messageTextField.clear();
            } else {
                ControllerUtils.showAlertDialog("Vui lòng chọn một cuộc trò chuyện trước khi gửi tin nhắn.", Alert.AlertType.ERROR, null);
                System.out.println("Vui lòng chọn một cuộc trò chuyện trước khi gửi tin nhắn.");
            }
        }
    }
}
