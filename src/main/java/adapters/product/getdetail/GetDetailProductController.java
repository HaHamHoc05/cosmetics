package adapters.product.getdetail;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.products.getdetail.GetDetailProductReq;
import cosmetic.usecase.products.getdetail.GetDetailProductRes; 

public class GetDetailProductController {
    private final InputBoundary<GetDetailProductReq, GetDetailProductRes> useCase;

    public GetDetailProductController(InputBoundary<GetDetailProductReq, GetDetailProductRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(Long productId) {
        GetDetailProductReq req = new GetDetailProductReq();
        req.productId = productId;
        useCase.execute(req);
    }
}