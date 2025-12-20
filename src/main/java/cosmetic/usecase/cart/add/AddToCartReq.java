package cosmetic.usecase.cart.add;

import cosmetic.usecase.RequestData;

public class AddToCartReq extends RequestData{
    public Long userId;
    public Long productId;
    public int quantity;

    public AddToCartReq(Long userId, Long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
}