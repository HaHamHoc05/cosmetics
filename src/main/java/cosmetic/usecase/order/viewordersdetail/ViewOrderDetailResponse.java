package cosmetic.usecase.order.viewordersdetail;

import cosmetic.ResponseData;
import cosmetic.entities.Order;

public class ViewOrderDetailResponse extends ResponseData {
    private final Order order;
    public ViewOrderDetailResponse(Order order){ this.order = order; }
    public Order getOrder(){ return order; }
}