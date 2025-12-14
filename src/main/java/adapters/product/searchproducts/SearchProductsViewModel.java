package adapters.product.searchproducts;

import java.util.List;
import java.util.stream.Collectors;

import cosmetic.entities.Product;

public class SearchProductsViewModel {
	public static class ProductData {
        private final Long id;
        private final String name;
        private final String description;
        private final double price;
        private final int stock;
        private final String status;
        private final String categoryName;

        public ProductData(Product p){
            this.id = p.getId();
            this.name = p.getName();
            this.description = p.getDescription();
            this.price = p.getPrice();
            this.stock = p.getStock();
            this.status = p.getStatus().getDisplayName();
            this.categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
        }

        public Long getId(){ return id; }
        public String getName(){ return name; }
        public String getDescription(){ return description; }
        public double getPrice(){ return price; }
        public int getStock(){ return stock; }
        public String getStatus(){ return status; }
        public String getCategoryName(){ return categoryName; }
    }

    private final List<ProductData> products;

    public SearchProductsViewModel(List<Product> productList){
        this.products = productList.stream().map(ProductData::new).collect(Collectors.toList());
    }

    public List<ProductData> getProducts(){ return products; }
}