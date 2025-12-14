package adapters.cart.updatecart;

import cosmetic.entities.Cart;

public class UpdateCartViewModel {
	private Cart cart;
    public void setCart(Cart cart){ this.cart=cart; }
    public Cart getCart(){ return cart; }
}
