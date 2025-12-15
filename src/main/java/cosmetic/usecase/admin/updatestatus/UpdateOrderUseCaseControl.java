package cosmetic.usecase.admin.updatestatus;

import cosmetic.entities.Order;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.repository.UserRepository;

public class UpdateOrderUseCaseControl implements UpdateOrderInputInterface {
	private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final UpdateOrderOutputInterface outputInterface;

    public UpdateOrderUseCaseControl(OrderRepository oRepo, UserRepository uRepo, UpdateOrderOutputInterface out) {
        this.orderRepo = oRepo;
        this.userRepo = uRepo;
        this.outputInterface = out;
    }

    @Override
    public void execute(UpdateOrderInputData input) {
        try {
            // Kiểm tra quyền Admin
            User requester = userRepo.findById(input.getRequesterId());
            if (requester == null || !requester.isAdmin()) {
                outputInterface.present(new UpdateOrderOutputData(false, "Truy cập bị từ chối!", null));
                return;
            }

            // Tìm đơn hàng
            Order order = orderRepo.findById(input.getOrderId());
            if (order == null) {
                outputInterface.present(new UpdateOrderOutputData(false, "Đơn hàng không tồn tại", null));
                return;
            }

            //
            String action = input.getAction().toUpperCase();
            switch (action) {
                case "CONFIRM":
                    order.confirm();
                    break;
                case "SHIP":
                    order.ship();
                    break;
                case "COMPLETE":
                    order.complete();
                    break;
                case "CANCEL":
                    order.cancel();
 
                    break;
                default:
                    throw new RuntimeException("Hành động không hợp lệ: " + action);
            }


            orderRepo.save(order);

            // Trả kết quả
            outputInterface.present(new UpdateOrderOutputData(true, "Cập nhật thành công", order.getStatus().toString()));

        } catch (Exception e) {
            outputInterface.present(new UpdateOrderOutputData(false, e.getMessage(), null));
        }
    }
}

