package cosmetic.usecase.order.updatestatus;

import cosmetic.entities.Order;
import cosmetic.entities.OrderStatus;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class UpdateStatusUseCase implements UseCase<UpdateStatusReq, UpdateStatusRes> {

    private final OrderRepository orderRepo;
    private final OutputBoundary<UpdateStatusRes> outputBoundary;

    public UpdateStatusUseCase(OrderRepository orderRepo, OutputBoundary<UpdateStatusRes> outputBoundary) {
        this.orderRepo = orderRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UpdateStatusReq req) {
        UpdateStatusRes res = new UpdateStatusRes();
        try {
            Order order = orderRepo.findById(req.orderId);
            if (order == null) throw new RuntimeException("Đơn hàng không tìm thấy");

            OrderStatus newStatus = OrderStatus.valueOf(req.newStatus);
            order.changeStatus(newStatus); // Entity check logic

            // Gọi hàm đã có trong Interface
            orderRepo.updateStatus(req.orderId, newStatus);

            res.success = true;
            res.message = "Cập nhật thành công";

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
        }

        if (outputBoundary != null) outputBoundary.present(res);
    }
}