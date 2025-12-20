package adapters.product.getdetail;

import adapters.Publisher;
import cosmetic.usecase.products.getdetail.GetDetailProductRes;

public class GetDetailProductViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public GetDetailProductRes productDetail;

    public void setState(boolean isSuccess, String message, GetDetailProductRes productDetail) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.productDetail = productDetail;
        notifySubscribers();
    }
}