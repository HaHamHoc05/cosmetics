package adapters.order.vieworder;

import java.util.List;
import java.util.stream.Collectors;

import cosmetic.usecase.order.vieworders.ViewOrdersOutputBoundary;
import cosmetic.usecase.order.vieworders.ViewOrdersResponse;

public class ViewOrdersPresenter implements ViewOrdersOutputBoundary {
    private ViewOrdersViewModel viewModel;
    @Override
    public void present(ViewOrdersResponse response){
        List<ViewOrdersViewModel.OrderSummary> summaries = response.getOrders().stream()
                .map(o -> new ViewOrdersViewModel.OrderSummary(o.getId(), o.getTotalAmount(), o.getStatus().getDisplayName()))
                .collect(Collectors.toList());
        this.viewModel = new ViewOrdersViewModel(summaries);
    }
    public ViewOrdersViewModel getViewModel(){ return viewModel; }
}