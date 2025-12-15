package cosmetic.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    
    private String shippingAddress;
    private String phone;
    private String paymentMethod;

    public Order() {
        this.items = new ArrayList<>();
    }

    // BR

    //Tạo Order từ Cart.
    //Thực hiện logic trừ tồn kho và snapshot giá.

    public static Order createFromCart(Cart cart, User user, String address, String phone, String paymentMethod) {
        if (cart == null || cart.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể thanh toán");
        }
        if (user == null) {
            throw new RuntimeException("Vui lòng đăng nhập để thanh toán");
        }

        Order order = new Order();
        order.userId = user.getId();
        order.status = OrderStatus.PENDING;
        order.createdAt = LocalDateTime.now();
        order.shippingAddress = address;
        order.phone = phone;
        order.paymentMethod = paymentMethod;

        for (CartItem ci : cart.getItems()) {
            //  Trừ tồn kho 
            ci.getProduct().reduceStock(ci.getQuantity());

            // Tạo OrderItem lưu giá hiện tại
            order.items.add(new OrderItem(ci.getProduct(), ci.getQuantity()));
        }

        order.calculateTotal();
        return order;
    }

    private void calculateTotal() {
        this.totalAmount = items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    // logic trạng thái
    public void confirm() {
        if (this.status != OrderStatus.PENDING) throw new RuntimeException("Chỉ đơn hàng mới mới được xác nhận");
        this.status = OrderStatus.CONFIRMED;
    }

    public void ship() {
        if (this.status != OrderStatus.CONFIRMED) throw new RuntimeException("Đơn hàng chưa xác nhận");
        this.status = OrderStatus.SHIPPING;
    }
    
    public void complete() {
        if (this.status != OrderStatus.SHIPPING) throw new RuntimeException("Đơn hàng chưa được giao");
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPING || this.status == OrderStatus.DELIVERED) {
            throw new RuntimeException("Không thể hủy đơn hàng đã/đang giao");
        }
        this.status = OrderStatus.CANCELLED;
        
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}