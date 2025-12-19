package cosmetic.usecase.order.updatestatus;

import cosmetic.entities.OrderStatus;
import cosmetic.usecase.RequestData;

public class UpdateStatusReq extends RequestData {
	public Long orderId;
	public OrderStatus status;
	
	public UpdateStatusReq(Long orderId, OrderStatus status) {
		this.orderId = orderId;
		this.status = status;
	}
}
