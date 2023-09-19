package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.conversation.ConversationDaoImpl;
import hung.pj.login.dao.message.MessageDaoImpl;
import hung.pj.login.dao.participant.ParticipantDaoImpl;
import hung.pj.login.model.ConversationModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessengerController implements Initializable {
    private UserSingleton userSingleton;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    ConversationDaoImpl conversationDao = new ConversationDaoImpl(connectionProvider.getConnection());
    ParticipantDaoImpl participantDao = new ParticipantDaoImpl(connectionProvider.getConnection());
    MessageDaoImpl messageDao = new MessageDaoImpl(connectionProvider.getConnection());
    @FXML
    private ListView<ConversationModel> conversationListView;

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


        System.out.println(userConversations.toString());
    }
}
