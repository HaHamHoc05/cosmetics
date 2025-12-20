package cosmetic.usecase.order.getlist;

import java.util.List;

import cosmetic.usecase.ResponseData;

public class GetListRes extends ResponseData {
    public boolean success;
    public String message;
    
    public List<OrderDTO> orders; // Danh sách các DTO
}