package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Category;

public interface CategoryRepository {
	Category findById(Long id);
    List<Category> findAll();
    void save(Category category);
    void update(Category category);
    void delete(Long id);
}