package cosmetic.repository;

import cosmetic.entities.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
    void save(Category category);
    Category findById(Long id);
}