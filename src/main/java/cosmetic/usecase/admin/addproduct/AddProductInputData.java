package cosmetic.usecase.admin.addproduct;

public class AddProductInputData {
	private Long requesterId; 
    private String name;
    private double price;
    private String description;
    private String image;
    private int stockQuantity;
    private Long categoryId;
    
    public AddProductInputData(Long requesterId, String name, double price, String description, String image, int stockQuantity, Long categoryId) {
        this.requesterId = requesterId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
    }

    public Long getRequesterId() { return requesterId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public int getStockQuantity() { return stockQuantity; }
    public Long getCategoryId() { return categoryId; }

}
