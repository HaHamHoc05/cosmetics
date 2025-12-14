package adapters.order.vieworderdetail;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderDetailViewModel {
    private String orderId;
    private String orderDate;
    private String status;
    private String totalAmount;
    private String paymentMethod;
    private String shippingAddress;
    private List<Item> items = new ArrayList<>();

    // Getters & Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    // Inner class để chứa dữ liệu từng dòng trong bảng
    public static class Item {
        public String productName;
        public String price;
        public int quantity;
        public String subtotal;

        public Item(String productName, String price, int quantity, String subtotal) {
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
        // Cần getter để JTable có thể đọc được dữ liệu qua Reflection (nếu dùng thư viện binding)
        // Hoặc truy cập trực tiếp nếu là public
        public String getProductName() { return productName; }
        public String getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public String getSubtotal() { return subtotal; }
    }
}