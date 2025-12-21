	package adapters.order.create;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.order.create.CreateOrderReq;
import cosmetic.usecase.order.create.CreateOrderRes;

public class CreateOrderController {
	
	private final InputBoundary<CreateOrderReq, CreateOrderRes> inputBoundary;
	
	public CreateOrderController(InputBoundary<CreateOrderReq, CreateOrderRes> inputBoundary) {
		this.inputBoundary = inputBoundary;
	}
	
	public static class InputDTO {
		public Long userId;
		public String address;
		public String phone;
		public String paymentMethod;
	}
	
	public void execute(InputDTO inputDTO) {
        // 1. Chuyển đổi dữ liệu thô (String) sang dữ liệu nghiệp vụ (CreateOrderReq)
        CreateOrderReq req = new CreateOrderReq();
        
        try {
            // Ép kiểu String -> Long cho UserId
        	req.userId = inputDTO.userId;
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu nhập ID bậy, hoặc để UseCase lo
            req.userId = null; 
        }
        
        req.address = inputDTO.address;
        req.phone = inputDTO.phone;
        req.paymentMethod = inputDTO.paymentMethod;

        // 2. Gọi Use Case thực thi
        inputBoundary.execute(req);
    }
}
