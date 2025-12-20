package adapters.product.getlist;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.products.getlist.GetListProductReq;
import cosmetic.usecase.products.getlist.GetListProductRes; 

public class GetListProductController {
    private final InputBoundary<GetListProductReq, GetListProductRes> useCase;

    public GetListProductController(InputBoundary<GetListProductReq, GetListProductRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(String keyword, Long categoryId) {
        GetListProductReq req = new GetListProductReq();
        req.keyword = keyword;
        req.categoryId = categoryId;
        useCase.execute(req);
    }
}