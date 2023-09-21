package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.conversation.ConversationDaoImpl;
import hung.pj.login.dao.message.MessageDaoImpl;
import hung.pj.login.dao.participant.ParticipantDaoImpl;
import hung.pj.login.model.ConversationModel;
import hung.pj.login.model.MessageModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MessengerController implements Initializable {
    @FXML
    private VBox messageVBox;
    @FXML
    private TextField messageTextField;
    private UserSingleton userSingleton;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    ConversationDaoImpl conversationDao = new ConversationDaoImpl(connectionProvider.getConnection());
    ParticipantDaoImpl participantDao = new ParticipantDaoImpl(connectionProvider.getConnection());
    MessageDaoImpl messageDao = new MessageDaoImpl(connectionProvider.getConnection());
    @FXML
    private ListView<ConversationModel> conversationListView;
    private List<Label> messageLabels = new ArrayList<>();


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
                String formattedMessage = "Người dùng số "+senderName + ": " + message.getMessageText();
                Label label = new Label(formattedMessage);
                messageLabels.add(label);
                messageVBox.getChildren().add(label);
            }
        }
    }

    private String getSenderName(int senderId) {
        // Thay thế phương thức này bằng cách truy vấn tên của người gửi dựa trên senderId
        // Return tên của người gửi
        return "Người gửi"; // Sửa thành tên thực tế
    }


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

                // Gửi tin nhắn bằng cách sử dụng đối tượng messageDao
                messageDao.sendMessage(messageModel);

                // Sau khi gửi, cập nhật giao diện người dùng (nếu cần)
                String senderName = loggedInUser.getFullname();
                String formattedMessage = senderName + ": " + messageText;

                Label label = new Label(formattedMessage);
                messageLabels.add(label);
                messageVBox.getChildren().add(label);

                // Xóa nội dung trong ô nhập tin nhắn
                messageTextField.clear();
            } else {
                // Hiển thị thông báo hoặc xử lý nếu không có cuộc trò chuyện nào được chọn
                System.out.println("Vui lòng chọn một cuộc trò chuyện trước khi gửi tin nhắn.");
            }
        }
    }


}
