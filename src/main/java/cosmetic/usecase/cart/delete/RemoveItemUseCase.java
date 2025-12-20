package cosmetic.usecase.cart.delete;

import cosmetic.entities.Cart;
import cosmetic.repository.CartRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class RemoveItemUseCase implements UseCase<RemoveItemReq, RemoveItemRes> {
    private final CartRepository cartRepo;
    private final OutputBoundary<RemoveItemRes> outputBoundary;

    public RemoveItemUseCase(CartRepository cartRepo, OutputBoundary<RemoveItemRes> outputBoundary) {
        this.cartRepo = cartRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(RemoveItemReq req) {
        RemoveItemRes res = new RemoveItemRes();
        try {
            Cart cart = cartRepo.findByUserId(req.userId);
            if (cart != null) {
                cartRepo.removeItem(cart.getId(), req.productId);
                res.success = true;
                res.message = "Đã xóa sản phẩm khỏi giỏ.";
            } else {
                res.success = false;
                res.message = "Giỏ hàng không tồn tại.";
            }
        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
        }
        if (outputBoundary != null) outputBoundary.present(res);
    }
}