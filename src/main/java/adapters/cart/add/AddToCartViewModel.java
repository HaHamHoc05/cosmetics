package adapters.cart.add;

import adapters.Publisher;

public class AddToCartViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public int totalItems; // Để cập nhật số trên icon giỏ hàng

    public void setState(boolean isSuccess, String message, int totalItems) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.totalItems = totalItems;
        notifySubscribers();
    }
}