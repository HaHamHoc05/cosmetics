package adapters.product.getlist;

import adapters.Publisher;
import cosmetic.usecase.products.getlist.GetListProductRes;
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
}