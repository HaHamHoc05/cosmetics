package adapters.admin.addproduct;

public class AddProductViewModel {
	public String status;      
    public String message;     
    public Long newProductId;  

    public boolean isSuccessful() {
        return "SUCCESS".equals(status);
    }
    
    public void reset() {
        status = null;
        message = null;
        newProductId = null;
    }
}