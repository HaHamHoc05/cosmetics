package cosmetic.usecase.cart.updatecart;

import cosmetic.ResponseData;
import cosmetic.entities.Cart;

public class UpdateCartResponse extends ResponseData{
	 private final Cart cart;
	    public UpdateCartResponse(Cart cart){ this.cart=cart; }
	    public Cart getCart(){ return cart; }
	}
