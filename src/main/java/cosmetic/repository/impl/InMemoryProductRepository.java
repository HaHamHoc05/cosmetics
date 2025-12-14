package cosmetic.repository.impl;

import cosmetic.entities.Category;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> products = new ConcurrentHashMap<Long, Product>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public InMemoryProductRepository() {
        // Khởi tạo dữ liệu mẫu
        initSampleData();
    }

    private void initSampleData() {
        Category serum = new Category("Serum", "Tinh chất dưỡng da");
        serum.setId(1L);
        
        Category cleanser = new Category("Sữa rửa mặt", "Làm sạch da");
        cleanser.setId(2L);
        
        Category moisturizer = new Category("Kem dưỡng", "Dưỡng ẩm");
        moisturizer.setId(3L);

        save(new Product(null, "Serum Vitamin C", "Serum dưỡng trắng da", 450000, 50, serum));
        save(new Product(null, "Sữa Rửa Mặt CeraVe", "Làm sạch dịu nhẹ", 250000, 100, cleanser));
        save(new Product(null, "Kem Dưỡng La Roche-Posay", "Dưỡng ẩm chuyên sâu", 580000, 30, moisturizer));
        save(new Product(null, "Serum Niacinamide", "Làm mờ thâm nám", 380000, 75, serum));
        save(new Product(null, "Kem Chống Nắng Anessa", "SPF 50+ PA++++", 320000, 60, moisturizer));
    }

    @Override
    public Product findById(Long id) {
        return products.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<Product>(products.values());
    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String kw = keyword.toLowerCase();
        return products.values().stream()
                .filter(new java.util.function.Predicate<Product>() {
                    @Override
                    public boolean test(Product p) {
                        return p.getName().toLowerCase().contains(kw) ||
                               (p.getDescription() != null && p.getDescription().toLowerCase().contains(kw));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return findAll();
        }
        return products.values().stream()
                .filter(new java.util.function.Predicate<Product>() {
                    @Override
                    public boolean test(Product p) {
                        return p.getCategory() != null && p.getCategory().getId().equals(categoryId);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String keyword, Long categoryId) {
        List<Product> result = findAll();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            final String kw = keyword.toLowerCase();
            result = result.stream()
                    .filter(new java.util.function.Predicate<Product>() {
                        @Override
                        public boolean test(Product p) {
                            return p.getName().toLowerCase().contains(kw) ||
                                   (p.getDescription() != null && p.getDescription().toLowerCase().contains(kw));
                        }
                    })
                    .collect(Collectors.toList());
        }
        
        if (categoryId != null) {
            final Long catId = categoryId;
            result = result.stream()
                    .filter(new java.util.function.Predicate<Product>() {
                        @Override
                        public boolean test(Product p) {
                            return p.getCategory() != null && p.getCategory().getId().equals(catId);
                        }
                    })
                    .collect(Collectors.toList());
        }
        
        return result;
    }

    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            Long newId = idGenerator.getAndIncrement();
            // Create new product with ID
            Product newProduct = new Product(
                newId,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
            );
            products.put(newId, newProduct);
        } else {
            products.put(product.getId(), product);
        }
    }

    @Override
    public void update(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID không được null khi update");
        }
        products.put(product.getId(), product);
    }

    @Override
    public void delete(Long id) {
        products.remove(id);
    }
}