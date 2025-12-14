package cosmetic.entities;

public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private ProductStatus status;
    private Category category;

    // Constructor TỐI ƯU — KHÔNG CHO TRUYỀN STATUS
    public Product(Long id, String name, String description, double price, int stock, Category category) {
        validateName(name);
        validatePrice(price);
        validateStock(stock);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;

        updateStatusByStock();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public ProductStatus getStatus() { return status; }
    public Category getCategory() { return category; }

    // Setters có validate
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public void setStock(int stock) {
        validateStock(stock);
        this.stock = stock;
        updateStatusByStock();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // BUSINESS RULE (Domain Logic)
    public boolean isInStock() {
        return this.stock > 0;
    }

    public boolean hasEnoughStock(int quantity) {
        return this.stock >= quantity;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");

        if (!hasEnoughStock(quantity))
            throw new IllegalArgumentException("Không đủ hàng trong kho");

        this.stock -= quantity;
        updateStatusByStock();
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Số lượng tăng phải > 0");

        this.stock += quantity;
        updateStatusByStock();
    }

    // Internal validation - JAVA 8 COMPATIBLE
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        if (name.length() < 2 || name.length() > 200)
            throw new IllegalArgumentException("Tên sản phẩm phải từ 2–200 ký tự");
    }

    private void validatePrice(double price) {
        if (price <= 0)
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
    }

    private void validateStock(int stock) {
        if (stock < 0)
            throw new IllegalArgumentException("Số lượng kho không thể âm");
    }

    private void updateStatusByStock() {
        this.status = (this.stock == 0)
                ? ProductStatus.OUT_OF_STOCK
                : ProductStatus.AVAILABLE;
    }
}