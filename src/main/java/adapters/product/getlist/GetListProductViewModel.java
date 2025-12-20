package adapters.product.getlist;

import adapters.Publisher;
import adapters.Subscriber;
import cosmetic.usecase.products.getlist.GetListProductRes;
import desktop.GUIProductList;

import java.util.List;

public class GetListProductViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public List<GetListProductRes.ProductDTO> products;

    public void setState(boolean isSuccess, String message, List<GetListProductRes.ProductDTO> products) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.products = products;
        notifySubscribers();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribe(subscriber);
    }
}