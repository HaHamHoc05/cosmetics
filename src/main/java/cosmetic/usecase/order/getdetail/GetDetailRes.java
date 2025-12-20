package cosmetic.usecase.order.getdetail;

import java.util.List;

import cosmetic.usecase.ResponseData;

public class GetDetailRes extends ResponseData {
	public boolean success;
	public String message;
	
	public Long orderId;
    public String status;
    public String address;
    public String phone;
    public double totalAmount;
    
    public List<OrderItemDTO> items;

}
