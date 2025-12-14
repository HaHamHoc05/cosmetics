package cosmetic.usecase.order.checkout;

import adapters.order.checkout.CheckoutPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Order;
import cosmetic.entities.Product;
import cosmetic.repository.CartRepository;
import cosmetic.repository.OrderRepository;

public class CheckoutUseCase implements UseCase<CheckoutRequest, CheckoutResponse> {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CheckoutPublisher publisher;

    public CheckoutUseCase(CartRepository cartRepo, OrderRepository orderRepo, CheckoutPublisher publisher){
        this.cartRepository = cartRepo;
        this.orderRepository = orderRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(CheckoutRequest request, OutputBoundary<CheckoutResponse> output){
        Cart cart = cartRepository.findByUserId(request.getUserId());
        if(cart == null || cart.isEmpty()) throw new RuntimeException("Giỏ hàng trống");

        Order order = Order.createFromCart(cart, request.getPaymentMethod(), request.getShippingAddress());
        orderRepository.save(order);

        // Giảm tồn kho sản phẩm
        for(CartItem item: cart.getItems()){
            Product product = item.getProduct();
            product.reduceStock(item.getQuantity());
        }

        cart.clear();
        cartRepository.save(cart);

        CheckoutResponse response = new CheckoutResponse(order);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}
