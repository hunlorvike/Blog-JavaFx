package hung.pj.login.dao.category;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.model.CategoryModel;
import hung.pj.login.model.PostModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CategoryDaoImpl implements ICategoryDao {
    private Connection connection;

    public CategoryDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addCategory(CategoryModel tagModels) {
        String query = "INSERT INTO categories (name, creator_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tagModels.getName());
            preparedStatement.setInt(2, tagModels.getCreator_id());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CategoryModel> getAllCategory() {
        List<CategoryModel> categoryModels = new ArrayList<>();
        String query = "SELECT c.*, u.fullname AS category_creator\n" +
                "FROM categories c\n" +
                "JOIN users u ON c.creator_id = u.user_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                int creator_id = resultSet.getInt("creator_id");
                String creator_name = resultSet.getString("category_creator");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                CategoryModel categoryModel = new CategoryModel(id, name, creator_id, creator_name, created_at, updated_at);
                categoryModels.add(categoryModel);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching categories from the database.", e);
        }
        return categoryModels;
    }

    @Override
    public CategoryModel getCategoryById(int id) {
        String query = "SELECT * FROM categories WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    int creator_id = resultSet.getInt("creator_id");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    CategoryModel categoryModel = new CategoryModel(id, name, creator_id, created_at, updated_at);
                    return new CategoryModel(categoryId, name, creator_id, created_at, updated_at);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching category by ID from the database.", e);
        }

        return null; // Trả về null nếu không tìm thấy danh mục với ID tương ứng
    }


    @Override
    public List<CategoryModel> getCategoryByName(String partialName) {
        List<CategoryModel> categoryModels = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Sử dụng % ở cả hai đầu để tìm tất cả các danh mục chứa partialName
            preparedStatement.setString(1, "%" + partialName + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    int creator_id = resultSet.getInt("creator_id");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    CategoryModel categoryModel = new CategoryModel(id, name, creator_id, created_at, updated_at);
                    categoryModels.add(categoryModel);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching categories by name from the database.", e);
        }
        return categoryModels;
    }


    @Override
    public boolean updateCategory(int categoryId, CategoryModel categoryModel) {
        String query = "UPDATE categories SET name = ? WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryModel.getName());
            preparedStatement.setInt(2, categoryId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating category in the database.", e);
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting category from the database.", e);
        }
    }


    @Override
    public int getCategoryUsageCount(int categoryId) {
        int categoryUsageCount = 0;
        String query = "SELECT COUNT(*) FROM post WHERE category = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, getCategoryNameById(categoryId));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryUsageCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryUsageCount;
    }

    @Override
    public List<CategoryModel> getCategoriesCreatedWithinLast7Days() {
        List<CategoryModel> categories = new ArrayList<>();
        Timestamp sevenDaysAgo = getTimestampForSevenDaysAgo();
        String query = "SELECT * FROM categories WHERE created_at >= ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, sevenDaysAgo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                int creator_id = resultSet.getInt("creator_id");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                CategoryModel categoryModel = new CategoryModel(id, name, creator_id, created_at, updated_at);
                categories.add(categoryModel);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching categories created within last 7 days from the database.", e);
        }

        return categories;
    }

    @Override
    public CategoryModel getMostUsedCategory() {
        CategoryModel mostUsedCategory = null;
        int maxUsageCount = -1;
        String query = "SELECT category_id FROM categories";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                int usageCount = getCategoryUsageCount(categoryId);

                if (usageCount > maxUsageCount) {
                    maxUsageCount = usageCount;
                    mostUsedCategory = getCategoryById(categoryId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching the most used category from the database.", e);
        }

        return mostUsedCategory;
    }

    @Override
    public String getCategoryNameById(int categoryId) {
        String categoryName = null;
        String query = "SELECT name FROM categories WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching category name by ID from the database.", e);
        }

        return categoryName;
    }


    @Override
    public CategoryModel getLeastUsedCategory() {
        CategoryModel leastUsedCategory = null;
        int minUsageCount = Integer.MAX_VALUE;
        String query = "SELECT category_id FROM categories";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                int usageCount = getCategoryUsageCount(categoryId);

                if (usageCount < minUsageCount) {
                    minUsageCount = usageCount;
                    leastUsedCategory = getCategoryById(categoryId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching the least used category from the database.", e);
        }

        return leastUsedCategory;
    }


    private Timestamp getTimestampForSevenDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        return new Timestamp(calendar.getTimeInMillis());
    }

}
