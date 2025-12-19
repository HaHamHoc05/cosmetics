package cosmetic.usecase.order.create;

import cosmetic.usecase.RequestData;

public class CreateOrderReq extends RequestData {
    public Long userId;           
    public String address;        
    public String phone;          
    public String paymentMethod;  
}