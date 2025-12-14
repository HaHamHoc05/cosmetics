package cosmetic.usecase.order.vieworders;

import java.util.List;

import adapters.order.vieworder.ViewOrdersPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;

public class ViewOrdersUseCase implements UseCase<ViewOrdersRequest, ViewOrdersResponse> {
    private final OrderRepository orderRepository;
    private final ViewOrdersPublisher publisher;

    public ViewOrdersUseCase(OrderRepository orderRepo, ViewOrdersPublisher publisher){
        this.orderRepository = orderRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(ViewOrdersRequest request, OutputBoundary<ViewOrdersResponse> output){
        List<Order> orders = orderRepository.findByUserId(request.getUserId());
        ViewOrdersResponse response = new ViewOrdersResponse(orders);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}