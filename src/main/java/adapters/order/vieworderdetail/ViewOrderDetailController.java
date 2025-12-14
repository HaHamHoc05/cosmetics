package adapters.order.vieworderdetail;

import cosmetic.repository.OrderRepository;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailRequest;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailUseCase;

public class ViewOrderDetailController {
	private final ViewOrderDetailUseCase useCase;
    private final ViewOrderDetailPresenter presenter;

    public ViewOrderDetailController(OrderRepository orderRepo, ViewOrderDetailPublisher publisher){
        this.presenter = new ViewOrderDetailPresenter();
        this.useCase = new ViewOrderDetailUseCase(orderRepo, publisher);
    }

    public ViewOrderDetailViewModel handle(Long orderId, Long userId){
        ViewOrderDetailRequest request = new ViewOrderDetailRequest(orderId, userId);
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}