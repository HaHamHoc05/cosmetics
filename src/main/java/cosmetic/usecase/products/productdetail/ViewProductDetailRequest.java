package cosmetic.usecase.products.productdetail;

import cosmetic.RequestData;

public class ViewProductDetailRequest extends RequestData {
    private final Long productId;

    public ViewProductDetailRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}