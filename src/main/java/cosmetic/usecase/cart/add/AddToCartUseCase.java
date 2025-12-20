package cosmetic.usecase.cart.add;

import cosmetic.entities.Cart;
import cosmetic.entities.Product;
import cosmetic.repository.CartRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class AddToCartUseCase implements UseCase<AddToCartReq, AddToCartRes> {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final OutputBoundary<AddToCartRes> outputBoundary;

    public AddToCartUseCase(CartRepository cartRepo, 
                            ProductRepository productRepo, 
                            OutputBoundary<AddToCartRes> outputBoundary) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(AddToCartReq req) {
        AddToCartRes res = new AddToCartRes();
        try {
            if (req.quantity <= 0) throw new IllegalArgumentException("Số lượng phải > 0");

            Product product = productRepo.findById(req.productId);
            if (product == null) throw new RuntimeException("Sản phẩm không tồn tại");
            
            // Logic kiểm tra tồn kho cơ bản
            if (product.getQuantity() < req.quantity) {
                throw new RuntimeException("Kho không đủ hàng");
            }

            Cart cart = cartRepo.findByUserId(req.userId);
            if (cart == null) {
                cart = cartRepo.createCart(req.userId);
            }

            cartRepo.addItem(cart.getId(), req.productId, req.quantity);

            res.success = true;
            res.message = "Đã thêm vào giỏ!";
            res.totalItems = cartRepo.countItems(cart.getId());

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}