package adapters.order.getlist;

import adapters.Publisher;
import adapters.Subscriber;
import cosmetic.usecase.order.getlist.OrderDTO;
import java.util.List;

public class GetListViewModel extends Publisher {
    public List<OrderDTO> orders;
    public String errorMessage;
    public boolean isSuccess; // SỬA: Thêm field này
    
    public void clear() {
        this.orders = null;
        this.errorMessage = null;
        this.isSuccess = false;
    }
    
    // SỬA: Thêm method alias để tương thích với GUI
    public void addSubscriber(Subscriber subscriber) {
        subscribe(subscriber);
    }
}