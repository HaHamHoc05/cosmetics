package cosmetic.usecase.order.updatestatus;

import org.glassfish.jersey.client.RequestEntityProcessing;

import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class UpdateStatusUseCase implements UseCase<UpdateStatusReq, UpdateStatusRes>{
	
	private final OrderRepository orderRepo;
	private final OutputBoundary<UpdateStatusRes> outputBoundary;
	
	public UpdateStatusUseCase(OrderRepository orderRepo, OutputBoundary<UpdateStatusRes> outputBoundary) {
		this.orderRepo = orderRepo;
		this.outputBoundary = outputBoundary;
	}
	

	@Override
	public void execute(UpdateStatusReq req) {
		UpdateStatusRes res = new UpdateStatusRes();
		
		try {
			//tim don hang trong DB
			Order order = orderRepo.findById(req.orderId);
			if (order == null) {
				throw new RuntimeException("Không tìm thấy đơn hàng với ID: " +req.orderId );
				
			}
			// goi entity thuc hien nv
			order.updateStatus(req.status);
			// lưu
			orderRepo.update(order);
			
			//tbao thanh cong
			res.success = true;
			res.message = "Cập nhật trạng thái thành công: " + req.status;
			
		} catch (Exception e) {	
			res.success = false;
			res.message = e.getMessage();
		}
		if (outputBoundary != null) {
            outputBoundary.present(res);
        }	
	} 
}
