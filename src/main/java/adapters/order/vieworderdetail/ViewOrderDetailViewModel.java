package adapters.order.vieworderdetail;

import java.util.List;
import java.util.stream.Collectors;

import cosmetic.entities.Order;
import cosmetic.entities.OrderItem;

public class ViewOrderDetailViewModel {
	public static class Item {
        private final Long productId;
        private final String productName;
        private final int quantity;
        private final double unitPrice;

        public Item(OrderItem item){
            this.productId = item.getProductId();
            this.productName = item.getProductName();
            this.quantity = item.getQuantity();
            this.unitPrice = item.getUnitPrice();
        }

        public Long getProductId(){ return productId; }
        public String getProductName(){ return productName; }
        public int getQuantity(){ return quantity; }
        public double getUnitPrice(){ return unitPrice; }
    }

    private final Long orderId;
    private final String status;
    private final double totalAmount;
    private final List<Item> items;

    public ViewOrderDetailViewModel(Order order){
        this.orderId = order.getId();
        this.status = order.getStatus().getDisplayName();
        this.totalAmount = order.getTotalAmount();
        this.items = order.getItems().stream().map(Item::new).collect(Collectors.toList());
    }

    public Long getOrderId(){ return orderId; }
    public String getStatus(){ return status; }
    public double getTotalAmount(){ return totalAmount; }
    public List<Item> getItems(){ return items; }
}