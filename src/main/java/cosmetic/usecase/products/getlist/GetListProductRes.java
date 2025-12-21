package cosmetic.usecase.products.getlist;

import java.util.List;
import cosmetic.usecase.ResponseData;

public class GetListProductRes extends ResponseData {
    public boolean success;
    public String message;
    public List<ProductDTO> products;

    public static class ProductDTO {
        public Long id;
        public String name;
        public double price;
        public String imageUrl;
        public Integer quantity; 
        public String description; 
        public Long categoryId; 
        
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
}