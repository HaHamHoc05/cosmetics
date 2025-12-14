package adapters.order.checkout;

import cosmetic.entities.Order;

public class CheckoutViewModel {
	private final Long orderId;
    private final Double totalAmount;
    private final String status;

    public CheckoutViewModel(Order order){
        this.orderId = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus().getDisplayName();
    }

    public Long getOrderId(){ return orderId; }
    public Double getTotalAmount(){ return totalAmount; }
    public String getStatus(){ return status; }
}