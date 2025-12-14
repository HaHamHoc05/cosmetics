package adapters.cart.addtocart;

import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.cart.addtocart.AddToCartRequest;
import cosmetic.usecase.cart.addtocart.AddToCartUseCase;

public class AddToCartController {
	 private final AddToCartUseCase useCase;
	 private final AddToCartPresenter presenter;

	 public AddToCartController(CartRepository cartRepo, ProductRepository productRepo, AddToCartPublisher publisher){
	        this.presenter = new AddToCartPresenter();
	        this.useCase = new AddToCartUseCase(cartRepo, productRepo, publisher);
	    }

	    public AddToCartViewModel handle(Long userId, Long productId, int quantity){
	        AddToCartRequest request = new AddToCartRequest(userId, productId, quantity);
	        useCase.execute(request, presenter);
	        return presenter.getViewModel();
	    }
	}