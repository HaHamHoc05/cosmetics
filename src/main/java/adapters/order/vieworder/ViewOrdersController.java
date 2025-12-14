package adapters.order.vieworder;

import cosmetic.repository.OrderRepository;
import cosmetic.usecase.order.vieworders.ViewOrdersRequest;
import cosmetic.usecase.order.vieworders.ViewOrdersUseCase;

public class ViewOrdersController {
	private final ViewOrdersUseCase useCase;
    private final ViewOrdersPresenter presenter;

    public ViewOrdersController(OrderRepository orderRepo, ViewOrdersPublisher publisher){
        this.presenter = new ViewOrdersPresenter();
        this.useCase = new ViewOrdersUseCase(orderRepo, publisher);
    }

    public ViewOrdersViewModel handle(Long userId){
        ViewOrdersRequest request = new ViewOrdersRequest(userId);
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}