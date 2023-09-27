package hung.pj.login.dao.category;

import hung.pj.login.model.CategoryModel;

import java.util.List;

public interface ICategoryDao {
    boolean addCategory(CategoryModel categoryModels);

    List<CategoryModel> getAllCategory();

    CategoryModel getCategoryById(int id);

    List<CategoryModel> getCategoryByName(String categoryName);

    boolean updateCategory(int categoryId, CategoryModel categoryModel);

    boolean deleteCategory(int categoryId);

    int getCategoryUsageCount(int categoryId);

    List<CategoryModel> getCategoriesCreatedWithinLast7Days();

    CategoryModel getMostUsedCategory();

    String getCategoryNameById(int categoryId);

    CategoryModel getLeastUsedCategory();
}
