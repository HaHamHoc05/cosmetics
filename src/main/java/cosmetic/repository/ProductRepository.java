package cosmetic.repository;

import cosmetic.entities.Product;
import java.util.List;

public interface ProductRepository {
    void save(Product product); 
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findByNameContaining(String keyword); 
    List<Product> findByCategoryId(Long categoryId);   
}