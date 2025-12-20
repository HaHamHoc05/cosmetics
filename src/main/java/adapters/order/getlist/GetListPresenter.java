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
        // Chỉ cần đẩy list DTO sang ViewModel là xong
        viewModel.orders = response.orders;
        viewModel.notifySubscribers();
    }
}