package cosmetic.usecase.order.vieworders;

import java.util.List;

import cosmetic.ResponseData;
import cosmetic.entities.Order;

public class ViewOrdersResponse extends ResponseData {
    private final List<Order> orders;
    public ViewOrdersResponse(List<Order> orders){ this.orders = orders; }
    public List<Order> getOrders(){ return orders; }
}