package cosmetic.usecase.order.vieworders;

import cosmetic.RequestData;

public class ViewOrdersRequest extends RequestData {
    private final Long userId;
    public ViewOrdersRequest(Long userId){ this.userId = userId; }
    public Long getUserId(){ return userId; }
}