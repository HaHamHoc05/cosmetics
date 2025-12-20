package adapters.category.getlist;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.category.getlist.GetListCategoryReq;
import cosmetic.usecase.category.getlist.GetListCategoryRes;

public class GetListCategoryController {
    private final InputBoundary<GetListCategoryReq, GetListCategoryRes> useCase;

    public GetListCategoryController(InputBoundary<GetListCategoryReq, GetListCategoryRes> useCase) {
        this.useCase = useCase;
    }

    public void execute() {
        GetListCategoryReq req = new GetListCategoryReq();
        useCase.execute(req);
    }
}