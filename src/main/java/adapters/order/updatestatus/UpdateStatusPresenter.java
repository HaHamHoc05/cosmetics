package adapters.order.updatestatus;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.updatestatus.UpdateStatusRes;

public class UpdateStatusPresenter implements OutputBoundary<UpdateStatusRes> {
    private final UpdateStatusViewModel viewModel;

    public UpdateStatusPresenter(UpdateStatusViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateStatusRes res) {
        viewModel.success = res.success;
        viewModel.message = res.message;
        viewModel.notifySubscribers();
    }
}