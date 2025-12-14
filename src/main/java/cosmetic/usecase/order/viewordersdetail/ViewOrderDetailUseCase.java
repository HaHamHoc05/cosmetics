package cosmetic.usecase.order.viewordersdetail;

import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;

public class ViewOrderDetailUseCase implements UseCase<ViewOrderDetailRequest, ViewOrderDetailResponse> {
    private final OrderRepository orderRepository;

    public ViewOrderDetailUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(ViewOrderDetailRequest request, OutputBoundary<ViewOrderDetailResponse> output) {
        if (request.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID không được để trống");
        }

        Order order = orderRepository.findById(request.getOrderId());

        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + request.getOrderId());
        }
        
        // Đảm bảo Order đã có Items (MySQLOrderRepository đã lo việc này)
        ViewOrderDetailResponse response = new ViewOrderDetailResponse(order);
        output.present(response);
    }
}