package cosmetic.usecase.admin.addproduct;


public class AddProductOutputData {
    private boolean success;
    private String message;
    private Long newProductId;

    // Constructor thành công
    public AddProductOutputData(Long newProductId, String message) {
        this.success = true;
        this.newProductId = newProductId;
        this.message = message;
    }

    // Constructor thất bại
    public AddProductOutputData(String errorMessage) {
        this.success = false;
        this.message = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getNewProductId() { return newProductId; }
}