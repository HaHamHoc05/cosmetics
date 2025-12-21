package adapters.cart.view;

import adapters.Publisher;
import adapters.Subscriber;
import cosmetic.usecase.cart.view.CartDetailDTO;
import java.math.BigDecimal;
import java.util.List;

public class ViewCartViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public List<CartDetailDTO> items;
    public Double grandTotal;

    public void setState(boolean isSuccess, String message, List<CartDetailDTO> items, Double grandTotal) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.items = items;
        this.grandTotal = grandTotal;
        notifySubscribers();
    }
    
    // SỬA: Thêm method alias để tương thích với GUI
    public void addSubscriber(Subscriber subscriber) {
        subscribe(subscriber);
    }
}