package cosmetic.usecase.cart.addtocart;

import cosmetic.ResponseData;
import cosmetic.entities.Cart;

public class AddToCartResponse extends ResponseData {
	private final Cart cart;
	public AddToCartResponse(Cart cart) { this.cart = cart; }

    public Cart getCart() { return cart; }
}
