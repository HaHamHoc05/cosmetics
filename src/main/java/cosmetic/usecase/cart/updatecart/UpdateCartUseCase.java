package cosmetic.usecase.cart.updatecart;

import adapters.cart.updatecart.UpdateCartPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Product;
import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;

public class UpdateCartUseCase implements UseCase<UpdateCartRequest, UpdateCartResponse> {
	private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UpdateCartPublisher publisher;

    public UpdateCartUseCase(CartRepository cartRepo, ProductRepository productRepo, UpdateCartPublisher publisher){
        this.cartRepo=cartRepo; 
        this.productRepo=productRepo; 
        this.publisher=publisher;
    }

    @Override
    public void execute(UpdateCartRequest request, OutputBoundary<UpdateCartResponse> output){
        Cart cart = cartRepo.findByUserId(request.getUserId());
        if(cart==null) throw new RuntimeException("Giỏ hàng không tồn tại");

        CartItem item = cart.findItemByProductId(request.getProductId());
        if(item==null) throw new RuntimeException("Sản phẩm không có trong giỏ hàng");

        Product product = item.getProduct();
        if(request.getQuantity() <= 0) cart.removeItem(request.getProductId());
        else if(product.getStock() < request.getQuantity())
            throw new RuntimeException("Số lượng trong kho không đủ");
        else item.setQuantity(request.getQuantity());

        cartRepo.save(cart);
        UpdateCartResponse response = new UpdateCartResponse(cart);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}