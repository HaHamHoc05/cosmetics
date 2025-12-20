package adapters.order.getdetail;

import adapters.Publisher;
import cosmetic.usecase.order.getdetail.OrderItemDTO;
import java.util.List;

public class GetDetailViewModel extends Publisher {
    public boolean success;
    public String message;
    
    // Thông tin chung
    public String orderIdText;
    public String statusText;
    public String totalAmountText;
    public String addressText;
    
    // Danh sách món hàng
    public List<OrderItemDTO> items;
}