package cosmetic.usecase.cart.view;

import cosmetic.entities.Cart;
import cosmetic.repository.CartRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;
import java.math.BigDecimal;
import java.util.Collections;

public class ViewCartUseCase implements UseCase<ViewCartReq, ViewCartRes> {
    private final CartRepository cartRepo;
    private final OutputBoundary<ViewCartRes> outputBoundary;

    public ViewCartUseCase(CartRepository cartRepo, OutputBoundary<ViewCartRes> outputBoundary) {
        this.cartRepo = cartRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ViewCartReq req) {
        ViewCartRes res = new ViewCartRes();
        try {
            Cart cart = cartRepo.findByUserId(req.userId);
            if (cart == null) {
                res.items = Collections.emptyList();
                res.grandTotal = BigDecimal.ZERO;
            } else {
                res.items = cartRepo.getCartDetails(cart.getId());
                res.grandTotal = res.items.stream()
                        .map(item -> item.totalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            res.success = true;
        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }
        if (outputBoundary != null) outputBoundary.present(res);
    }
}