package cosmetic.repository;
import cosmetic.entities.Product;
import java.util.List;

public interface ProductRepository {
	Product findById(Long id);
    List<Product> findAll();
    List<Product> findByKeyword(String keyword);
    List<Product> findByCategoryId(Long categoryId);
    void save(Product product);
    void update(Product product);
    void delete(Long id);
	List<Product> search(String keyword, Long categoryId);
}