package cosmetic.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private User user; 
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

    // --- FACTORY METHOD ---
    public static Order createFromCart(Cart cart, User user, String address, String phone, String paymentMethod) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setShippingAddress(address);
        order.setPhone(phone);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getPrice()); 
            orderItems.add(item);
        }
        order.setItems(orderItems);
        order.calculateTotal();
        
        return order;
    }

    // --- BUSINESS LOGIC ---
    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    
    // SỬA: Đổi tên method từ updateStatus -> changeStatus để khớp với UseCase
    public void changeStatus(OrderStatus newStatus) {
        // Business Rule: Không được cập nhật đơn đã hủy
        if (this.status == OrderStatus.CANCELLED) {
            throw new RuntimeException("Không thể cập nhật đơn hàng đã hủy!");
        }
        this.status = newStatus;
    }
    
    // Giữ lại method cũ để tương thích
    public void updateStatus(OrderStatus newStatus) {
        changeStatus(newStatus);
    }
    
    // --- GETTERS & SETTERS ---
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
		
}