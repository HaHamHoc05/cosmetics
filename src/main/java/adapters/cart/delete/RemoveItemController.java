package adapters.cart.delete;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.cart.delete.RemoveItemReq;
import cosmetic.usecase.cart.delete.RemoveItemRes; 

public class RemoveItemController {
    private final InputBoundary<RemoveItemReq, RemoveItemRes> useCase;

    public RemoveItemController(InputBoundary<RemoveItemReq, RemoveItemRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(Long userId, Long productId) {
        RemoveItemReq req = new RemoveItemReq(userId, productId);
        useCase.execute(req);
    }
}