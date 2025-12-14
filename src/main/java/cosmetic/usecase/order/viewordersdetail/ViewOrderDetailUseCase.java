package cosmetic.usecase.order.viewordersdetail;

import adapters.order.vieworderdetail.ViewOrderDetailPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;

public class ViewOrderDetailUseCase implements UseCase<ViewOrderDetailRequest, ViewOrderDetailResponse> {
    private final OrderRepository orderRepo;
    private final ViewOrderDetailPublisher publisher;

    public ViewOrderDetailUseCase(OrderRepository orderRepo, ViewOrderDetailPublisher publisher){
        this.orderRepo = orderRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(ViewOrderDetailRequest request, OutputBoundary<ViewOrderDetailResponse> output){
        Order order = orderRepo.findById(request.getOrderId());
        if(order == null || !order.belongsToUser(request.getUserId()))
            throw new RuntimeException("Không có quyền truy cập đơn hàng");

        ViewOrderDetailResponse response = new ViewOrderDetailResponse(order);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}
