package cosmetic.usecase.cart.updatecart;

import cosmetic.RequestData;

public class UpdateCartRequest extends RequestData{
	private final Long userId;
    private final Long productId;
    private final int quantity;
    public UpdateCartRequest(Long userId, Long productId, int quantity){
        this.userId=userId; this.productId=productId; this.quantity=quantity;
    }
    public Long getUserId(){ return userId; }
    public Long getProductId(){ return productId; }
    public int getQuantity(){ return quantity; }
}
