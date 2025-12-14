package cosmetic.usecase.order.checkout;

import cosmetic.ResponseData;
import cosmetic.entities.Order;

public class CheckoutResponse extends ResponseData {
    private final Order order;
    public CheckoutResponse(Order order){ this.order = order; }
    public Order getOrder(){ return order; }
}