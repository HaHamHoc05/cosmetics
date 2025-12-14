package adapters.order.vieworder;

import java.util.List;

public class ViewOrdersViewModel {
	public static class OrderSummary {
        private final Long orderId;
        private final double totalAmount;
        private final String status;

        public OrderSummary(Long orderId, double totalAmount, String status){
            this.orderId = orderId;
            this.totalAmount = totalAmount;
            this.status = status;
        }

        public Long getOrderId(){ return orderId; }
        public double getTotalAmount(){ return totalAmount; }
        public String getStatus(){ return status; }
    }

    private final List<OrderSummary> orders;
    public ViewOrdersViewModel(List<OrderSummary> orders){ this.orders = orders; }
    public List<OrderSummary> getOrders(){ return orders; }
}