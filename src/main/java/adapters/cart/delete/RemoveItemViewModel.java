package adapters.cart.delete;

import adapters.Publisher;
import adapters.Subscriber;

public class RemoveItemViewModel extends Publisher {
    public boolean isSuccess;
    public String message;

    public void setState(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        notifySubscribers();
    }
    public void addSubscriber(Subscriber subscriber) {
        subscribe(subscriber);
    }
}
