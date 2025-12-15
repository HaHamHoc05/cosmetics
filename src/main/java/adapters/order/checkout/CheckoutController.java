package adapters.order.checkout;

import cosmetic.repository.CartRepository;
import cosmetic.repository.OrderRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.order.checkout.CheckoutRequest;
import cosmetic.usecase.order.checkout.CheckoutUseCase;

public class CheckoutController {
	private final CheckoutUseCase useCase;
    private final CheckoutPresenter presenter;

    public CheckoutController(CartRepository cartRepo, OrderRepository orderRepo, 
            ProductRepository productRepo, CheckoutPublisher publisher){
        this.presenter = new CheckoutPresenter();
        this.useCase = new CheckoutUseCase(cartRepo, orderRepo, 
                 productRepo, publisher);
    }

    public CheckoutViewModel handle(Long userId, String paymentMethod, String shippingAddress){
        CheckoutRequest request = new CheckoutRequest(userId, paymentMethod, shippingAddress);
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}
