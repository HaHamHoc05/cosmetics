package cosmetic.usecase.cart.add;

import cosmetic.usecase.ResponseData;

public class AddToCartRes extends ResponseData {
    public boolean success;
    public String message;
    public int totalItems; // Để update số lượng trên icon giỏ hàng
}