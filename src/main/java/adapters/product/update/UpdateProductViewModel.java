// File: UpdateProductViewModel.java
package adapters.product.update;
import adapters.Publisher;
import adapters.Subscriber;

public class UpdateProductViewModel extends Publisher {
    public boolean isSuccess;
    public String message;

    public void setState(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        notifySubscribers();
    }
    public void addSubscriber(Subscriber s) { subscribe(s); }
}



// File: UpdateProductController.java
