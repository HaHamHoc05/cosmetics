package adapters.cart.add;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.cart.add.AddToCartReq;
import cosmetic.usecase.cart.add.AddToCartRes; 

public class AddToCartController {
    private final InputBoundary<AddToCartReq, AddToCartRes> useCase;

    public AddToCartController(InputBoundary<AddToCartReq, AddToCartRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(InputDTO input) {
        AddToCartReq req = new AddToCartReq(input.userId, input.productId, input.quantity);
        useCase.execute(req);
    }

    // DTO để nhận dữ liệu từ UI (Desktop/Web)
    public static class InputDTO {
        public Long userId;
        public Long productId;
        public int quantity;
    }
}