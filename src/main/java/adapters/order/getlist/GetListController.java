package adapters.order.getlist;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.order.getlist.GetListReq;
import cosmetic.usecase.order.getlist.GetListRes;

public class GetListController {
    private final InputBoundary<GetListReq, GetListRes> inputBoundary;

    public GetListController(InputBoundary<GetListReq, GetListRes> inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(Long userId) {
        GetListReq req = new GetListReq(userId);
        inputBoundary.execute(req);
    }
    

    public void executeForAdmin() {
        execute(null); // userId = null → Lấy tất cả
    }
    

    public void executeForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID không được null khi gọi từ User");
        }
        execute(userId);
    }
}