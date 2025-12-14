package adapters.order.vieworderdetail;

import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailOutputBoundary;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailResponse;

public class ViewOrderDetailPresenter implements ViewOrderDetailOutputBoundary {
    private ViewOrderDetailViewModel viewModel;
    @Override
    public void present(ViewOrderDetailResponse response){
        this.viewModel = new ViewOrderDetailViewModel(response.getOrder());
    }
    public ViewOrderDetailViewModel getViewModel(){ return viewModel; }
}
