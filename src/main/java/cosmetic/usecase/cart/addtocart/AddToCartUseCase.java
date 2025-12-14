package cosmetic.usecase.cart.addtocart;

import adapters.cart.addtocart.AddToCartPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Cart;
import cosmetic.entities.Product;
import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;

public class AddToCartUseCase implements UseCase<AddToCartRequest, AddToCartResponse> {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final AddToCartPublisher publisher;

    public AddToCartUseCase(CartRepository cartRepo, ProductRepository productRepo, AddToCartPublisher publisher){
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(AddToCartRequest request, OutputBoundary<AddToCartResponse> output){
        Cart cart = cartRepo.findByUserId(request.getUserId());
        if(cart == null) cart = new Cart(request.getUserId());

        Product product = productRepo.findById(request.getProductId());
        if(product == null) throw new RuntimeException("Sản phẩm không tồn tại");

        try { cart.addItem(product, request.getQuantity()); }
        catch(Exception e){ throw new RuntimeException(e.getMessage()); }

        cartRepo.save(cart);
        AddToCartResponse response = new AddToCartResponse(cart);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}
