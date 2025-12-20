package cosmetic.usecase.cart.delete;

import cosmetic.usecase.RequestData;

public class RemoveItemReq extends RequestData {
    public Long userId;
    public Long productId;

    public RemoveItemReq(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}