package cosmetic.usecase.order.viewordersdetail;

import cosmetic.RequestData;

public class ViewOrderDetailRequest extends RequestData {
    private final Long orderId;
    private final Long userId;

    public ViewOrderDetailRequest(Long orderId, Long userId){
        this.orderId = orderId;
        this.userId = userId;
    }

    public Long getOrderId(){ return orderId; }
    public Long getUserId(){ return userId; }
}
