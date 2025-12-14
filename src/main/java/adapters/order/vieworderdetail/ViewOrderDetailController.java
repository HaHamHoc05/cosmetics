package adapters.order.vieworderdetail;

import cosmetic.repository.OrderRepository;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailRequest;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailUseCase;

import javax.swing.*;

public class ViewOrderDetailController {
    private final OrderRepository orderRepository;

    public ViewOrderDetailController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ViewOrderDetailViewModel handle(Long orderId) {
        try {
            // 1. Chuẩn bị Request
            if (orderId == null) {
                throw new IllegalArgumentException("Mã đơn hàng không hợp lệ!");
            }

            ViewOrderDetailRequest request = new ViewOrderDetailRequest(orderId, orderId);

            // 2. Khởi tạo Presenter (Output Boundary)
            ViewOrderDetailPresenter presenter = new ViewOrderDetailPresenter();

            // 3. Khởi tạo UseCase và thực thi
            ViewOrderDetailUseCase useCase = new ViewOrderDetailUseCase(orderRepository);
            useCase.execute(request, presenter);

            // 4. Trả về ViewModel đã có dữ liệu đẹp
            return presenter.getViewModel();

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
            JOptionPane.showMessageDialog(null, "Lỗi khi xem chi tiết đơn hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null; // Trả về null để GUI biết mà không hiển thị
        }
    }
}