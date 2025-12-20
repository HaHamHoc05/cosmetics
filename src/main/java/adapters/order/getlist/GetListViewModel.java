package adapters.order.getlist;

import adapters.Publisher;
import cosmetic.usecase.order.getlist.OrderDTO;
import java.util.List;

public class GetListViewModel extends Publisher {
    public List<OrderDTO> orders; // Dữ liệu để hiện lên bảng (Table)
    public String errorMessage;
    
    public void clear() {
        this.orders = null;
        this.errorMessage = null;
    }
}