package adapters.order.getdetail;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.order.getdetail.GetDetailReq;
import cosmetic.usecase.order.getdetail.GetDetailRes;

public class GetDetailController {
    private final InputBoundary<GetDetailReq, GetDetailRes> inputBoundary;

    public GetDetailController(InputBoundary<GetDetailReq, GetDetailRes> inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(Long orderId, Long userId) {
        GetDetailReq req = new GetDetailReq(orderId, userId);
        inputBoundary.execute(req);
    }
}