package cosmetic.usecase.order.create;

import cosmetic.usecase.ResponseData;

public class CreateOrderRes extends ResponseData {
    public boolean success;       // Thành công hay thất bại?
    public String message;        // Thông báo lỗi hoặc thành công
    public Long newOrderId;       // Mã đơn hàng vừa tạo
}