package cosmetic.usecase.order.getlist;

public class OrderDTO {
    public Long id;
    public String status;
    public double totalAmount;
    public String customerName;
    public String createdAt;
    public String address;
    public String paymentMethod;

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public String getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return address; } // Khớp với JSP
    public String getPaymentMethod() { return paymentMethod; }
}