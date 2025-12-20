package cosmetic.usecase.order.getdetail;

import java.util.ArrayList;

import cosmetic.entities.Order;
import cosmetic.entities.Role;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetDetailUseCase implements UseCase<GetDetailReq, GetDetailRes>{
	
	private final OrderRepository orderRepo;
	private final OutputBoundary<GetDetailRes> outputBoundary;
	
	public GetDetailUseCase(OrderRepository orderRepo, OutputBoundary<GetDetailRes> outputBoundary) {
		this.orderRepo = orderRepo;
		this.outputBoundary = outputBoundary;
	}
	
	@Override
	public void execute(GetDetailReq req) {
		GetDetailRes res = new GetDetailRes();
		
		try {
			Order order = orderRepo.findById(req.orderId);
			
			if (order == null) {
				throw new RuntimeException("Đơn hàng không tồn tại.");
				
			}
			
			if (!order.getUserId().equals(req.userId)) {
                throw new RuntimeException("Bạn không có quyền xem đơn hàng này.");
            }
			
			res.orderId = order.getId();
            res.status = order.getStatus().toString();
            res.address = order.getShippingAddress();
            res.phone = order.getPhone();
            res.totalAmount = order.getTotalAmount();
            
            res.items = new ArrayList<>();
            
            for (OrderItem itemEntity : order.getItems()) { 
                OrderItemDTO itemDTO = new OrderItemDTO();
                
                itemDTO.productId = itemEntity.getProductId();
                itemDTO.quantity = itemEntity.getQuantity();
                itemDTO.price = itemEntity.getPrice();
                
                // Tính thành tiền cho từng món (Logic hiển thị)
                itemDTO.subTotal = itemEntity.getPrice() * itemEntity.getQuantity();
                
                res.items.add(itemDTO);
            }
            res.success = true;
		} catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
		}
	
		outputBoundary.present(res);

}
