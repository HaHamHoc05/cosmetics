package adapters.order.getdetail;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.getdetail.GetDetailRes;

public class GetDetailPresenter implements OutputBoundary<GetDetailRes> {
    private final GetDetailViewModel viewModel;

    public GetDetailPresenter(GetDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetDetailRes res) {
        viewModel.success = res.success;
        viewModel.message = res.message;

        if (res.success) {
            // Format dữ liệu cho đẹp mắt trước khi hiện lên UI
            viewModel.orderIdText = "Đơn hàng #" + res.orderId;
            viewModel.statusText = "Trạng thái: " + res.status;
            viewModel.totalAmountText = String.format("%,.0f VNĐ", res.totalAmount); // Ví dụ: 100,000 VNĐ
            viewModel.addressText = res.address;
            viewModel.items = res.items;
        }
        
        viewModel.notifySubscribers();
    }
}