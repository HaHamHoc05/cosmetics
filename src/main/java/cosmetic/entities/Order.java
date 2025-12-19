package cosmetic.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private double totalAmount;

    private OrderStatus status; 
    private String shippingAddress;
    private String phone;
    private String paymentMethod;
    private LocalDateTime createdAt;
    

    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING; 
    }

    /**
     * LOGIC NGHIỆP VỤ: Tạo Order từ Cart
     * - Chuyển đổi item trong giỏ thành item trong đơn hàng.
     * - Snapshot (chụp lại) giá bán tại thời điểm mua.
     * - Tính tổng tiền.
     */
    public static Order createFromCart(Cart cart, User user, String address, String phone, String paymentMethod) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Không thể tạo đơn hàng từ giỏ hàng trống!");
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setShippingAddress(address);
        order.setPhone(phone);
        order.setPaymentMethod(paymentMethod);
        
        double total = 0;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice()); // Lưu giá tại thời điểm mua
            
            order.getItems().add(orderItem);
            total += cartItem.getPrice() * cartItem.getQuantity();
        }
        order.setTotalAmount(total);
        
        return order;
    }
   
   public void updateStatus(OrderStatus status) {
	   if (this.status == OrderStatus.CANCELLED) {
		   throw new RuntimeException("Đơn hàng đã hủy, không thể cập nhật trạng thái khác.");
		   
	   }
	   if (this.status == OrderStatus.DELIVERED && status == OrderStatus.CANCELLED) {
		   throw new RuntimeException("Đơn hàng giao thành công, không thể hủy.");
	   }
	   
	   this.status = status;
   }
   

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}