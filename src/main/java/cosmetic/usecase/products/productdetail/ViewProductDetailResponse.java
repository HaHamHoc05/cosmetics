package cosmetic.usecase.products.productdetail;

import cosmetic.ResponseData;
import cosmetic.entities.Product;

public class ViewProductDetailResponse extends ResponseData {
    private final Product product;

    public ViewProductDetailResponse(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}