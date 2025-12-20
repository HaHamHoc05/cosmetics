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
    private LocalDateTime createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    // --- BUSINESS LOGIC ---

    // Cập nhật trạng thái an toàn
    public void changeStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new RuntimeException("Đơn hàng đã hủy, không thể cập nhật trạng thái.");
        }
        if (this.status == OrderStatus.SHIPPING && newStatus == OrderStatus.PENDING) {
            throw new RuntimeException("Đơn hàng đang giao không thể quay lại chờ xử lý.");
        }
        this.status = newStatus;
    }

    // Tính lại tổng tiền (đề phòng sai sót)
    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}