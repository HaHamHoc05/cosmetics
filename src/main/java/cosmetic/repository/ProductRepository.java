package cosmetic.repository;

import java.util.List;

import cosmetic.entities.Product;

public interface ProductRepository {
    List<Product> findAll();
    
    Product findById(Long id);
    
    List<Product> searchByName(String keyword);
    
    List<Product> findByCategory(Long categoryId);
    
    void updateQuantity(Long productId, int newQuantity);
}