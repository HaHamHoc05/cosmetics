package adapters.product.create;

import adapters.Publisher;

public class CreateProductViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public Long newProductId;

    public void setState(boolean isSuccess, String message, Long newProductId) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.newProductId = newProductId;
        notifySubscribers();
    }
}