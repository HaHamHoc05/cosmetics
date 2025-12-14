package cosmetic.usecase.order.checkout;

import cosmetic.RequestData;

public class CheckoutRequest extends RequestData {
    private final Long userId;
    private final String paymentMethod;
    private final String shippingAddress;

    public CheckoutRequest(Long userId, String paymentMethod, String shippingAddress){
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
    }

    public Long getUserId(){ return userId; }
    public String getPaymentMethod(){ return paymentMethod; }
    public String getShippingAddress(){ return shippingAddress; }
}