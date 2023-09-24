package hung.pj.login.controller;

import com.jfoenix.controls.JFXComboBox;
import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.conversation.ConversationDaoImpl;
import hung.pj.login.dao.participant.ParticipantDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.exception.UnauthorizedAccessException;
import hung.pj.login.model.ConversationModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberController implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    ConversationDaoImpl conversationDao = new ConversationDaoImpl(connectionProvider.getConnection());
    ParticipantDaoImpl participantDao = new ParticipantDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton;
    // Khai báo biến loggedInUser
    UserModel loggedInUser;
    public Button allButton, superAdminButton, adminButton, moderatorButton;
    @FXML
    private TableView<UserModel> tableView;

    @FXML
    private TableColumn<UserModel, Integer> idColumn;

    @FXML
    private TableColumn<UserModel, String> nameColumn;

    @FXML
    private TableColumn<UserModel, String> emailColumn;

    @FXML
    private TableColumn<UserModel, String> roleColumn;

    @FXML
    private TableColumn<UserModel, Timestamp> createColumn;

    @FXML
    private TableColumn<UserModel, Timestamp> updateColumn;
    @FXML
    private MenuItem followUserMenuItem;

    private ObservableList<UserModel> userModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        loggedInUser = userSingleton.getLoggedInUser();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView();

        // Tạo context menu
        ContextMenu menu = new ContextMenu();
        // Tạo các option cho menu vừa tạo
        MenuItem message = new MenuItem("Nhắn tin");
        message.setOnAction(event -> {
            try {
                messageStart();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        MenuItem followUserMenuItem = new MenuItem("Theo dõi/Huỷ");
        followUserMenuItem.setOnAction(event -> followUser());
        MenuItem editRoleItem = new MenuItem("Cập nhật role");
        editRoleItem.setOnAction(event -> editRoleItem());
        MenuItem lockUserAccount = new MenuItem("Khoá tài khoản");
        lockUserAccount.setOnAction(event -> lockUserAccount());
        MenuItem unlockUserAccount = new MenuItem("Mở khoá tài khoản");
        unlockUserAccount.setOnAction(event -> unlockUserAccount());
        MenuItem viewUserDetail = new MenuItem("Xem chi tiết");
        viewUserDetail.setOnAction(event -> {
            try {
                viewUserDetail();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tableView.setContextMenu(menu);

        // Gán các option cho menu
        menu.getItems().addAll(message, followUserMenuItem, editRoleItem, lockUserAccount, unlockUserAccount, viewUserDetail);
        this.followUserMenuItem = followUserMenuItem;

        // Ẩn menu khi chuột trái được nhấp bên ngoài menu
        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Đây là hành động khi người dùng nhấp chuột trái vào một bản ghi
                UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();

                int followerUserId = selectedUser.getUser_id();
                int userId = loggedInUser.getUser_id();

                // Kiểm tra xem đã theo dõi hay chưa
                if (userDao.isFollowing(userId, followerUserId)) {
                    followUserMenuItem.setText("Hủy theo dõi"); // Đặt nhãn "Hủy theo dõi" khi thực hiện theo dõi
                } else {
                    followUserMenuItem.setText("Theo dõi"); // Đặt nhãn "Theo dõi" khi hủy theo dõi
                }

            }
            if (event.getButton() == MouseButton.PRIMARY) {
                menu.hide();
            }
        });

    }

    private void messageStart() throws SQLException, IOException {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();

        // Kiểm tra xem cuộc trò chuyện đã tồn tại giữa hai người dùng chưa
        int conversationId = conversationDao.getExistingConversationId(loggedInUser.getUser_id(), selectedUser.getUser_id());

        if (conversationId == -1) {
            // Nếu cuộc trò chuyện chưa tồn tại, tạo một đối tượng ConversationModel
            ConversationModel conversation = new ConversationModel();
            conversation.setConversationName(loggedInUser.getFullname() + " - " + selectedUser.getFullname());

            // Tạo cuộc trò chuyện
            conversationDao.createConversation(conversation);

            // Lấy conversationId sau khi tạo cuộc trò chuyện
            conversationId = conversationDao.getConversationIdByName(conversation.getConversationName());

            // Thêm người dùng hiện tại (loggedInUser) vào cuộc trò chuyện
            participantDao.addParticipant(loggedInUser.getUser_id(), conversationId);

            // Thêm người dùng đã chọn (selectedUser) vào cuộc trò chuyện
            participantDao.addParticipant(selectedUser.getUser_id(), conversationId);

            ControllerUtils.showAlertDialog("Tạo cuộc trò chuyện mới thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());

            AppMain.setRoot("messenger.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        } else {
            ControllerUtils.showAlertDialog("Đã tồn tại cuộc trò chuyện", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());

        }
    }


    private void followUser() {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            // Không có người dùng được chọn trong bảng
            return;
        }

        int followerUserId = selectedUser.getUser_id();
        int userId = loggedInUser.getUser_id();

        // Kiểm tra xem đã theo dõi hay chưa
        if (userDao.isFollowing(userId, followerUserId)) {

            // Nếu đã theo dõi, thực hiện hủy theo dõi
            userDao.unfollowUser(userId, followerUserId);
            ControllerUtils.showAlertDialog("Hủy theo dõi thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
        } else {
            // Nếu chưa theo dõi, thực hiện theo dõi
            userDao.followUser(userId, followerUserId);
            ControllerUtils.showAlertDialog("Theo dõi thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
        }
    }


    private void editRoleItem() {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedUser.toString());
        if (selectedUser == null) {
            return;
        }

        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        // Hiển thị dialog để chọn role mới
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Chọn Role Mới");
        dialog.setHeaderText("Chọn role mới cho người dùng " + selectedUser.getFullname());

        // Tạo một JFXComboBox để chọn role
        JFXComboBox<String> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll("Super Admin", "Admin", "Moderator"); // Danh sách role thực tế

        // Tạo nút OK và Cancel
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Đặt nội dung của dialog
        VBox vbox = new VBox(new Label("Chọn role mới:"), comboBox);
        dialog.getDialogPane().setContent(vbox);

        // Xử lý khi nút OK được bấm
        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                String selectedRole = comboBox.getValue(); // Thay cho choiceBox
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    try {
                        userDao.assignUserRole(selectedUser.getEmail(), selectedRole);
                        refreshTableView(); // Làm mới TableView sau khi cập nhật
                    } catch (UnauthorizedAccessException ex) {
                        // Xử lý khi người dùng không có quyền
                        System.out.println("Không có quyền cập nhật role: " + ex.getMessage());
                    }
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void lockUserAccount() {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            return;
        }

        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        // Hiển thị dialog để nhập thời gian khoá tài khoản
        Optional<String> result = ControllerUtils.showTextInputDialog("Nhập thời gian khoá tài khoản",
                "Nhập thời gian khoá tài khoản cho người dùng " + selectedUser.getFullname(),
                "Thời gian khoá (số phút):", rootAnchorPane.getScene().getWindow());
        if (result.isPresent()) {
            String minutesString = result.get();
            try {
                int minutes = Integer.parseInt(minutesString);
                LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(minutes);
                userDao.lockUserAccount(selectedUser.getEmail(), lockedUntil);
                ControllerUtils.refreshTableView(tableView, userDao.getAllUsers());
                ControllerUtils.showAlertDialog("Tài khoản đã bị khoá thành công.", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
            } catch (NumberFormatException e) {
                ControllerUtils.showAlertDialog("Vui lòng nhập một số nguyên hợp lệ cho thời gian khoá.", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }
        }
    }

    private void unlockUserAccount() {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            return;
        }

        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        userDao.unlockUserAccount(selectedUser.getEmail());
        ControllerUtils.refreshTableView(tableView, userDao.getAllUsers());
        ControllerUtils.showAlertDialog("Tài khoản đã được mở khóa.", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
    }

    private void viewUserDetail() throws IOException {
        UserModel selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String selectedId = selectedUser.getEmail();

            DataHolder.getInstance().setData(selectedId);
            AppMain.setRoot("profile_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        }
    }

    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, userDao.getAllUsers());
    }

    public void handleAllButtonClicked() {
        refreshTableView();
    }

    public void handleRoleButtonClicked(ActionEvent event) {
        String role = "";
        String buttonId = ((Button) event.getSource()).getId();

        switch (buttonId) {
            case "superAdminButton":
                role = "Super Admin";
                break;
            case "adminButton":
                role = "Admin";
                break;
            case "moderatorButton":
                role = "Moderator";
                break;
            default:
                return;
        }

        List<UserModel> userModelList = userDao.getUsersByRole(role);
        ControllerUtils.refreshTableView(tableView, userModelList);
    }
}
