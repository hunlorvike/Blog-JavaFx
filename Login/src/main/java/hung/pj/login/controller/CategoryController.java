package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.category.CategoryDaoImpl;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.CategoryModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.UserSingleton;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Button addCategoryButton;

    @FXML
    private TableView<CategoryModel> tableView;

    @FXML
    private TableColumn<CategoryModel, Integer> idColumn;

    @FXML
    private TableColumn<CategoryModel, String> nameColumn;

    @FXML
    private TableColumn<CategoryModel, Integer> creatorColumn;

    @FXML
    private TableColumn<CategoryModel, Timestamp> createColumn, updateColumn;

    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());
    CategoryDaoImpl categoryDao = new CategoryDaoImpl(connectionProvider.getConnection());
    private UserSingleton userSingleton;
    UserModel loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kiểm tra singleton đăng nhập
        userSingleton = UserSingleton.getInstance();
        loggedInUser = userSingleton.getLoggedInUser();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator_id"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        updateColumn.setCellValueFactory(new PropertyValueFactory<>("updated_at"));

        refreshTableView();

        // Tạo context menu
        ContextMenu menu = new ContextMenu();
        MenuItem editCategory = new MenuItem("Sửa danh mục");
        editCategory.setOnAction(event -> editCategory());
        MenuItem deleteCategory = new MenuItem("Xoá danh mục");
        deleteCategory.setOnAction(event -> deleteCategory());
        // Gán các option cho menu
        tableView.setContextMenu(menu);
        menu.getItems().addAll(editCategory, deleteCategory);

    }

    private void deleteCategory() {
        CategoryModel selectedCategory = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            return;
        }
        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        boolean isDelete = categoryDao.deleteCategory(selectedCategory.getCategoryId());
        if (isDelete) {
            ControllerUtils.showAlertDialog("Xoá danh mục thành công.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
        } else {
            ControllerUtils.showAlertDialog("Xoá danh mục thất bại.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
        }
        refreshTableView();


    }

    private void editCategory() {
        CategoryModel selectedCategory = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            return;
        }

        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        // Hiển thị dialog để nhập tên danh mục mới
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Thông báo sửa danh mục");
        dialog.setContentText("Tên danh mục mới:");

        // Gọi getCategoryById và đặt giá trị mặc định của trường nhập liệu
        CategoryModel category = categoryDao.getCategoryById(selectedCategory.getCategoryId());
        if (category != null) {
            dialog.getEditor().setText(category.getName());
        }

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String categoryString = result.get();
            try {
                CategoryModel newCategoryModel = new CategoryModel(categoryString);

                categoryDao.updateCategory(selectedCategory.getCategoryId(), newCategoryModel);
                ControllerUtils.showAlertDialog("Đã sửa danh mục thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
            } catch (NumberFormatException e) {
                ControllerUtils.showAlertDialog("Sửa danh mục thất bại", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }
        }
        refreshTableView();
    }

    private void refreshTableView() {
        ControllerUtils.refreshTableView(tableView, categoryDao.getAllCategory());
    }

    public void handleAddCategory() {
        // Kiểm tra quyền của người dùng hiện tại
        if (!userDao.isUserSuperAdmin(loggedInUser.getEmail())) {
            ControllerUtils.showAlertDialog("Không có quyền cập nhật chức năng này.", Alert.AlertType.WARNING, rootAnchorPane.getScene().getWindow());
            return;
        }

        // Hiển thị dialog để nhập thời gian khoá tài khoản
        Optional<String> result = ControllerUtils.showTextInputDialog("Add Category",
                "Thông báo nhập " + Constants.NO_DATA,
                "Tên danh mục:", rootAnchorPane.getScene().getWindow());
        if (result.isPresent()) {
            String categoryString = result.get();
            try {
                int creatorId = loggedInUser.getUser_id();

                CategoryModel categoryModel = new CategoryModel(categoryString, creatorId);
                categoryDao.addCategory(categoryModel);
                ControllerUtils.showAlertDialog("Đã tạo danh mục thành công", Alert.AlertType.INFORMATION, rootAnchorPane.getScene().getWindow());
            } catch (NumberFormatException e) {
                ControllerUtils.showAlertDialog("Tạo danh mục thất bại", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }
        }
        refreshTableView();

    }
}
