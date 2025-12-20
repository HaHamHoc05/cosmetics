package adapters.product.create;

import adapters.Publisher;
import adapters.Subscriber;

public class CreateProductViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public Long newProductId;
    
    // SỬA: Thêm alias cho productId (để tương thích với GUI cũ)
    public Long productId;

    public void setState(boolean isSuccess, String message, Long newProductId) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.newProductId = newProductId;
        this.productId = newProductId; // Đồng bộ
        notifySubscribers();
    }
    
    // SỬA: Thêm method alias để tương thích với GUI
    public void addSubscriber(Subscriber subscriber) {
        subscribe(subscriber);
    }
}