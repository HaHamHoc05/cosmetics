package adapters.cart.updatecart;

import cosmetic.OutputBoundary;
import cosmetic.ResponseData;
import cosmetic.usecase.cart.updatecart.UpdateCartOutputBoundary;
import cosmetic.usecase.cart.updatecart.UpdateCartResponse;

public class UpdateCartPresenter implements UpdateCartOutputBoundary {
	private final UpdateCartViewModel vm = new UpdateCartViewModel();
    @Override
    public void present(UpdateCartResponse response){ vm.setCart(response.getCart()); }
    public UpdateCartViewModel getViewModel(){ return vm; }
}