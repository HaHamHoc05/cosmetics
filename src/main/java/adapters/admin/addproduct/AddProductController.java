package adapters.admin.addproduct;

import cosmetic.usecase.admin.addproduct.AddProductInputData;
import cosmetic.usecase.admin.addproduct.AddProductInputInterface;

public class AddProductController {
	    private final AddProductInputInterface inputBoundary;

	    // Nhận uc đã được khởi tạo từ bên ngoài
	    public AddProductController(AddProductInputInterface inputBoundary) {
	        this.inputBoundary = inputBoundary;
	    }

	    public void execute(Long requesterId, String name, double price, String description, 
	                        String image, int stock, Long categoryId) {
	        
	        // Đóng gói dữ liệu thành InputData
	        AddProductInputData input = new AddProductInputData(
	            requesterId, name, price, description, image, stock, categoryId
	        );
	        
	        // Gọi UC xử lý
	        inputBoundary.execute(input);
	    }
	}