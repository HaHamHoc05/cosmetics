package adapters.cart.delete;

import adapters.Publisher;

public class RemoveItemViewModel extends Publisher {
    public boolean isSuccess;
    public String message;

    public void setState(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        notifySubscribers();
    }
}