package adapters.order.create;

import adapters.Publisher;
import adapters.Subscriber;

public class CreateOrderViewModel extends Publisher {
    // Dữ liệu hiển thị ra màn hình
    public String message;
    public String newOrderId;
    public boolean isSuccess;
    
    // SỬA: Sửa tên method để khớp với GUI
    public void addSubscriber(Subscriber subscriber) {
        // Gọi method cha (Publisher.subscribe)
        subscribe(subscriber);
    }
    
    // Reset dữ liệu cũ
    public void clear() {
        this.message = "";
        this.newOrderId = "";
        this.isSuccess = false;
    }
}