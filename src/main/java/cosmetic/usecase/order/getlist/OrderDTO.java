package cosmetic.usecase.order.getlist;

public class OrderDTO {
    public Long id;
    public String status;
    public double totalAmount;
    public String customerName;
    public String createdAt;
    public String address;
    public String paymentMethod; 
    
    public OrderDTO() {}
    
    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
    
    public double getTotalPrice() {
        return totalAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getShippingAddress() {
        return address;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
}
