package cosmetic.usecase.order.getlist;

import java.util.ArrayList;
import java.util.List;

import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetListUseCase implements UseCase<GetListReq, GetListRes>{
	private final OrderRepository orderRepo;
	private final OutputBoundary<GetListRes> outputBoundary;
	
	public GetListUseCase(OrderRepository orderRepo,OutputBoundary<GetListRes> outputBoundary) {
		this.orderRepo = orderRepo;
		this.outputBoundary = outputBoundary;
	}
	
	@Override
	public void execute(GetListReq req) {
		GetListRes res = new GetListRes();
		List<Order> entities;	 //bien de tam chua du lieu 
		
		
		//phan quyen du lieu
		if (req.userId != null) {
			// neu co id , chi lay don cua nguoi do
			entities = orderRepo.findByUserId(req.userId);
		} else {
			entities = orderRepo.findAll();
		}
		res.orders = new ArrayList<>();
		
		// duyen qua tung don hang
		if (entities != null ) {
			for (Order order : entities) {
				OrderDTO dto = new OrderDTO();
				
				dto.id = order.getId();
				dto.totalAmount = order.getTotalAmount();
				dto.status = order.getStatus().toString();
				
				if (order.getCreatedAt() != null) {
					dto.date = order.getCreatedAt().toString();
				}
				res.orders.add(dto);

			}
			
		}
		outputBoundary.present(res);
	}
}
