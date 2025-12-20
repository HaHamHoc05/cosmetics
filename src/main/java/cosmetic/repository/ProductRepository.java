package cosmetic.repository;

import java.util.List;

import cosmetic.entities.CartItem;
import cosmetic.entities.Product;

public interface ProductRepository {
    List<Product> findAll();
    
    Product findById(Long id);
    
    List<Product> searchByName(String keyword);
    
    List<Product> findByCategory(Long categoryId);
    
    void updateQuantity(Long productId, int newQuantity);
    void decreaseStockBatch(List<CartItem> items);

	void save(Product p);
	void update(Product p);
	void delete(Long id);
}