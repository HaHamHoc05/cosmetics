package adapters.cart.updatecart;

import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.cart.updatecart.UpdateCartRequest;
import cosmetic.usecase.cart.updatecart.UpdateCartUseCase;

public class UpdateCartController {
	private final UpdateCartUseCase useCase;
    private final UpdateCartPresenter presenter;
    public UpdateCartController(CartRepository cartRepo, ProductRepository productRepo, UpdateCartPublisher publisher){
        this.presenter=new UpdateCartPresenter();
        this.useCase=new UpdateCartUseCase(cartRepo,productRepo,publisher);
    }
    public UpdateCartViewModel handle(Long userId, Long productId, int quantity){
        UpdateCartRequest req=new UpdateCartRequest(userId,productId,quantity);
        useCase.execute(req,presenter);
        return presenter.getViewModel();
    }
}