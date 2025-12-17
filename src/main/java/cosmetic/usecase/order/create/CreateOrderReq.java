package cosmetic.usecase.order.create;

import cosmetic.usecase.RequestData;

public class CreateOrderReq extends RequestData {
    public Long userId;           // Khách hàng nào đặt?
    public String address;        // Giao đi đâu?
    public String phone;          // SĐT liên hệ?
    public String paymentMethod;  // Thanh toán kiểu gì?
}