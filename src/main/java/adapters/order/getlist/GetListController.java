package adapters.order.getlist;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.order.getlist.GetListReq;
import cosmetic.usecase.order.getlist.GetListRes;

public class GetListController {
    private final InputBoundary<GetListReq, GetListRes> inputBoundary;

    public GetListController(InputBoundary<GetListReq, GetListRes> inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    // Input là userId (Long), nếu null nghĩa là Admin muốn xem tất cả
    public void execute(Long userId) {
        GetListReq req = new GetListReq(userId);
        inputBoundary.execute(req);
    }
}