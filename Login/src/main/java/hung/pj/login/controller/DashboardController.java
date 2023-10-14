package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.category.CategoryDaoImpl;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.CategoryModel;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;

import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private ConnectionProvider connectionProvider = new ConnectionProvider();
    private UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    private PostDaoImpl postDao = new PostDaoImpl(connectionProvider.getConnection());
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl(connectionProvider.getConnection());
    @FXML
    private Label numberOfUsersLabel, numberOfSuperAdmin, numberOfAdmin, numberOfModerator, numberOfAllPosts, numberOfPublished, numberOfScheduled, numberOfDrafts, numberOfAllCategories, numberOfMostUsed, numberOfLeastUsed, numberOf7Day;

    @FXML
    private TableView<PostModel> tableViewPost;

    @FXML
    private TableColumn<PostModel, Integer> idColumn, idMemberColumn, viewColumn;

    @FXML
    private TableColumn<PostModel, String> titleColumn, statusColumn, categoryColumn, creatorColumn;

    @FXML
    private TableColumn<PostModel, Timestamp> createdAtColumn, updatedAtColumn;

    @FXML
    private TableView<UserModel> tableViewMember;

    @FXML
    private TableColumn<UserModel, String> nameColumn, postColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("post_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("view_count"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_name"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTablePostView();

        setDataTabPost();
        setDataTabMember();
        setDataTableMember();
        setDataTabCategory();

        // Tạo menu item
        MenuItem viewPostDetailMenuItem = new MenuItem("Xem chi tiết bài viết");
        viewPostDetailMenuItem.setOnAction(event -> {
            // Xử lý khi người dùng chọn xem chi tiết bài viết
            try {
                viewPostDetail();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Thêm menu item vào ContextMenu của tableViewPost
        ContextMenu postContextMenu = new ContextMenu();
        postContextMenu.getItems().add(viewPostDetailMenuItem);
        tableViewPost.setContextMenu(postContextMenu);

        // Tạo menu item
        MenuItem viewMemberDetailMenuItem = new MenuItem("Xem chi tiết thành viên");
        viewMemberDetailMenuItem.setOnAction(event -> {
            // Xử lý khi người dùng chọn xem chi tiết thành viên
            try {
                viewMemberDetail();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Thêm menu item vào ContextMenu của tableViewMember
        ContextMenu memberContextMenu = new ContextMenu();
        memberContextMenu.getItems().add(viewMemberDetailMenuItem);
        tableViewMember.setContextMenu(memberContextMenu);


    }

    private void viewPostDetail() throws IOException {
        PostModel selectedPost = tableViewPost.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            int selectedId = selectedPost.getPost_id();
            postDao.increaseViewCount(selectedId);
            DataHolder.getInstance().setData(String.valueOf(selectedId));
            AppMain.setRoot("post_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);

        }
    }

    private void viewMemberDetail() throws IOException {
        UserModel selectedUser = tableViewMember.getSelectionModel().getSelectedItem();
        System.out.println(selectedUser);
        if (selectedUser != null) {
            String selectedId = selectedUser.getEmail();

            DataHolder.getInstance().setData(selectedId);
            AppMain.setRoot("profile_detail.fxml", Constants.CUSTOM_WIDTH, Constants.CUSTOM_HEIGHT, false);
        }
    }


    private void refreshTablePostView() {
        ControllerUtils.refreshTableView(tableViewPost, postDao.getAllPosts());
    }

    private void setDataTabPost() {
        int totalPostsCount = postDao.getAllPosts().size();
        setLabelValue(numberOfAllPosts, totalPostsCount + " Posts");

        int publicPostsCount = postDao.getPostsByStatus("Published").size();
        setLabelValue(numberOfPublished, String.valueOf(publicPostsCount));

        int scheduledPostsCount = postDao.getPostsByStatus("Scheduled").size();
        setLabelValue(numberOfScheduled, String.valueOf(scheduledPostsCount));

        int draftPostsCount = postDao.getPostsByStatus("Draft").size();
        setLabelValue(numberOfDrafts, String.valueOf(draftPostsCount));
    }

    private void setDataTabCategory() {
        int totalCategoryCount = categoryDao.getAllCategory().size();
        setLabelValue(numberOfAllCategories, totalCategoryCount + " Categories");

        CategoryModel mostUsedCategory = categoryDao.getMostUsedCategory();
        if (mostUsedCategory != null) {
            String mostUsedCategoryName = mostUsedCategory.getName();
            setLabelValue(numberOfMostUsed, mostUsedCategoryName);
        } else {
            // Xử lý trường hợp không có dữ liệu trả về
            setLabelValue(numberOfMostUsed, "None");
        }

        CategoryModel leastUsedCategory = categoryDao.getLeastUsedCategory();
        if (leastUsedCategory != null) {
            String leastUsedCategoryName = leastUsedCategory.getName();
            setLabelValue(numberOfLeastUsed, leastUsedCategoryName);
        } else {
            // Xử lý trường hợp không có dữ liệu trả về
            setLabelValue(numberOfLeastUsed, "None");
        }

        int numberOf7Days = categoryDao.getCategoriesCreatedWithinLast7Days().size();
        setLabelValue(numberOf7Day, String.valueOf(numberOf7Days));

    }

    private void setDataTabMember() {
        int totalUsersCount = userDao.getAllUsers().size();
        setLabelValue(numberOfUsersLabel, totalUsersCount + " Members");

        int superAdminCount = userDao.getUsersByRole("Super Admin").size();
        setLabelValue(numberOfSuperAdmin, String.valueOf(superAdminCount));

        int adminCount = userDao.getUsersByRole("Admin").size();
        setLabelValue(numberOfAdmin, String.valueOf(adminCount));

        int moderatorCount = userDao.getUsersByRole("Moderator").size();
        setLabelValue(numberOfModerator, String.valueOf(moderatorCount));
    }

    private void setDataTableMember() {
        idMemberColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("postCount"));

        refreshTableViewMember();
    }

    private void refreshTableViewMember() {
        ControllerUtils.refreshTableView(tableViewMember, userDao.getUsersByPostCountDescending(15));
    }

    private void setLabelValue(Label label, String text) {
        label.setText(text);
    }
}