package cosmetic.entities;

public class Product {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String image;
    private int stockQuantity;
    private Long categoryId;
    private ProductStatus status;

    public Product(Long id, String name, double price, int stockQuantity, Long categoryId) {
        validateData(name, price, stockQuantity);
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.status = (stockQuantity > 0) ? ProductStatus.ACTIVE : ProductStatus.OUT_OF_STOCK;
    }
 // BR
    private void validateData(String name, double price, int stock) {
        if (name == null || name.trim().isEmpty()) 
            throw new RuntimeException("Tên sản phẩm không được để trống");
        if (price < 0) 
            throw new RuntimeException("Giá sản phẩm không được âm");
        if (stock < 0)
            throw new RuntimeException("Số lượng tồn kho không được âm");
    }

    //Kiểm tra xem có đủ hàng để bán không.

    public void checkAvailability(int quantityRequested) {
        if (this.status == ProductStatus.OUT_OF_STOCK || this.stockQuantity == 0) {
            throw new RuntimeException("Sản phẩm '" + this.name + "' đã hết hàng!");
        }
        if (this.stockQuantity < quantityRequested) {
            throw new RuntimeException("Sản phẩm '" + this.name + "' chỉ còn lại " + this.stockQuantity + " sản phẩm.");
        }
    }

    //Trừ kho 

    public void reduceStock(int quantitySold) {
        checkAvailability(quantitySold); // Check lại 
        this.stockQuantity -= quantitySold;
        
        if (this.stockQuantity == 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public int getStockQuantity() { return stockQuantity; }
    public ProductStatus getStatus() { return status; }
    public Long getCategoryId() { return categoryId; }
    
    public void setDescription(String description) { this.description = description; }
    public void setImage(String image) { this.image = image; }
}