package cosmetic.usecase.order.getlist;

import java.util.ArrayList;
import java.util.List;
import cosmetic.entities.Order;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetListUseCase implements UseCase<GetListReq, GetListRes> {

    private final OrderRepository orderRepo;
    private final OutputBoundary<GetListRes> outputBoundary;

    public GetListUseCase(OrderRepository orderRepo, OutputBoundary<GetListRes> outputBoundary) {
        this.orderRepo = orderRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GetListReq req) {
        GetListRes res = new GetListRes();
        try {
            if (req.userId == null) {
                throw new IllegalArgumentException("User ID không hợp lệ.");
            }

            List<Order> orders = orderRepo.findAllByUserId(req.userId);
            
            // Mapping Entity -> DTO
            res.orders = new ArrayList<>();
            for (Order o : orders) {
                OrderDTO dto = new OrderDTO();
                dto.id = o.getId();
                dto.totalAmount = o.getTotalAmount();
                dto.status = o.getStatus().name();
                dto.createdAt = o.getCreatedAt().toString();
                dto.address = o.getShippingAddress();
                res.orders.add(dto);
            }
            
            res.success = true;
            res.message = "Lấy danh sách thành công.";
            
        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }

        if (outputBoundary != null) outputBoundary.present(res);
    }
}