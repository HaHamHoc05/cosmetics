package adapters.order.getlist;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.getlist.GetListRes;

public class GetListPresenter implements OutputBoundary<GetListRes> {
    private final GetListViewModel viewModel;

    public GetListPresenter(GetListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetListRes response) {
        // SỬA: Cập nhật isSuccess
        viewModel.isSuccess = response.success;
        viewModel.orders = response.orders;
        viewModel.errorMessage = response.message;
        viewModel.notifySubscribers();
    }
}