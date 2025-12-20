package cosmetic.usecase.cart.view;

import cosmetic.usecase.RequestData;

public class ViewCartReq extends RequestData {
    public Long userId;

    public ViewCartReq(Long userId) {
        this.userId = userId;
    }
}