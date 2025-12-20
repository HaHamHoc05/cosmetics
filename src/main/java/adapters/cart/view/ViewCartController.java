package adapters.cart.view;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.cart.view.ViewCartReq;
import cosmetic.usecase.cart.view.ViewCartRes; 

public class ViewCartController {
    private final InputBoundary<ViewCartReq, ViewCartRes> useCase;

    public ViewCartController(InputBoundary<ViewCartReq, ViewCartRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(Long userId) {
        ViewCartReq req = new ViewCartReq(userId);
        useCase.execute(req);
    }
}