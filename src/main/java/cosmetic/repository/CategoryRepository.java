package cosmetic.repository;

import cosmetic.entities.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
    Category findById(Long id);
    void save(Category category);
}