package cosmetic.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private String paymentMethod;
    private double totalAmount;
    private String shippingAddress;
    private String cancelReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor mới
    public Order(Long userId, String paymentMethod, String shippingAddress) {
        validateUserId(userId);
        validatePaymentMethod(paymentMethod);
        validateShippingAddress(shippingAddress);
        
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalAmount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor từ database
    public Order(Long id, Long userId, List<OrderItem> items, OrderStatus status, 
                 String paymentMethod, double totalAmount, String shippingAddress,
                 String cancelReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public OrderStatus getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public String getCancelReason() { return cancelReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Validation
    private void validateUserId(Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("User ID không được để trống");
    }

    private void validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank())
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống");
        List<String> validMethods = List.of("COD", "BANKING", "MOMO", "VNPAY");
        if (!validMethods.contains(paymentMethod.toUpperCase()))
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
    }

    private void validateShippingAddress(String address) {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Địa chỉ giao hàng không được để trống");
        if (address.length() < 10)
            throw new IllegalArgumentException("Địa chỉ giao hàng tối thiểu 10 ký tự");
    }

    // Business methods
    public static Order createFromCart(Cart cart, String paymentMethod, String shippingAddress) {
        if (cart == null || cart.isEmpty())
            throw new IllegalArgumentException("Giỏ hàng trống");
        
        Order order = new Order(cart.getUserId(), paymentMethod, shippingAddress);
        
        for (CartItem cartItem : cart.getItems()) {
            order.addItem(cartItem.getProduct(), cartItem.getQuantity());
        }
        
        return order;
    }

    public void addItem(Product product, int quantity) {
        if (this.status != OrderStatus.PENDING)
            throw new IllegalArgumentException("Chỉ thêm sản phẩm khi đơn hàng đang chờ xử lý");
        
        OrderItem item = new OrderItem(product, quantity);
        this.items.add(item);
        this.totalAmount = calculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus))
            throw new IllegalArgumentException("Không thể chuyển từ '" + 
                status.getDisplayName() + "' sang '" + newStatus.getDisplayName() + "'");
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        updateStatus(OrderStatus.CONFIRMED);
    }

    public void ship() {
        updateStatus(OrderStatus.SHIPPING);
    }

    public void deliver() {
        updateStatus(OrderStatus.DELIVERED);
    }

    public void cancel(String reason) {
        if (!this.status.canCancel())
            throw new IllegalArgumentException("Không thể hủy đơn hàng ở trạng thái: " + status.getDisplayName());
        if (reason == null || reason.isBlank())
            throw new IllegalArgumentException("Vui lòng nhập lý do hủy đơn");
        
        this.status = OrderStatus.CANCELED;
        this.cancelReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelByUser(Long requestUserId, String reason) {
        if (!this.userId.equals(requestUserId))
            throw new IllegalArgumentException("Không có quyền hủy đơn hàng này");
        cancel(reason);
    }

    public boolean belongsToUser(Long userId) {
        return this.userId.equals(userId);
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public String getPaymentMethodDisplay() {
        switch (paymentMethod.toUpperCase()) {
            case "COD": return "Thanh toán khi nhận hàng";
            case "BANKING": return "Chuyển khoản ngân hàng";
            case "MOMO": return "Ví MoMo";
            case "VNPAY": return "VNPay";
            default: return paymentMethod;
        }
    }
}