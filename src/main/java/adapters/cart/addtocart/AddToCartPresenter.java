package adapters.cart.addtocart;

import cosmetic.usecase.cart.addtocart.AddToCartOutputBoundary;
import cosmetic.usecase.cart.addtocart.AddToCartResponse;

public class AddToCartPresenter implements AddToCartOutputBoundary {
    private final AddToCartViewModel vm = new AddToCartViewModel();
    
    @Override
    public void present(AddToCartResponse response){ vm.setCart(response.getCart()); }
    public AddToCartViewModel getViewModel(){ return vm; }
}