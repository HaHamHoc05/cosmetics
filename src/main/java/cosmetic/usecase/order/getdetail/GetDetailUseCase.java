package cosmetic.usecase.order.getdetail;

import java.util.ArrayList;
import cosmetic.entities.OrderItem;
import cosmetic.entities.Order;
import cosmetic.entities.Role;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetDetailUseCase implements UseCase<GetDetailReq, GetDetailRes>{
    
    private final OrderRepository orderRepo;
    private final OutputBoundary<GetDetailRes> outputBoundary;
    
    public GetDetailUseCase(OrderRepository orderRepo, OutputBoundary<GetDetailRes> outputBoundary) {
        this.orderRepo = orderRepo;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(GetDetailReq req) {
        GetDetailRes res = new GetDetailRes();
        
        try {
            // 1. Tìm đơn hàng
            Order order = orderRepo.findById(req.orderId);
            if (order == null) {
                throw new RuntimeException("Đơn hàng không tồn tại.");
            }

            // 2. [SỬA LỖI] Logic Phân Quyền (Business Rule)
            // Lấy thông tin người đang yêu cầu xem
            User requestUser = orderRepo.findUserById(req.userId);
            if (requestUser == null) {
                throw new RuntimeException("Người dùng không xác định.");
            }

            // Cho phép xem nếu: Là chủ đơn hàng HOẶC Là Admin (RoleID = 1)
            boolean isOwner = order.getUserId().equals(req.userId);
            boolean isAdmin = (requestUser.getRoleId() == 1); 

            if (!isOwner && !isAdmin) {
                throw new RuntimeException("Bạn không có quyền xem đơn hàng này.");
            }
            
            // 3. Map dữ liệu sang Response
            res.orderId = order.getId();
            res.status = order.getStatus().toString();
            res.address = order.getShippingAddress(); // Đảm bảo Entity Order có getter này
            res.phone = order.getPhone();
            res.totalAmount = order.getTotalAmount();
            
            res.items = new ArrayList<>();
            
            // 4. [SỬA LỖI] Kiểm tra null trước khi loop để tránh NullPointerException
            if (order.getItems() != null) {
                for (OrderItem itemEntity : order.getItems()) { 
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    
                    itemDTO.productId = itemEntity.getProductId();
                    itemDTO.quantity = itemEntity.getQuantity();
                    itemDTO.price = itemEntity.getPrice();
                    
                    // Tính thành tiền hiển thị
                    itemDTO.subTotal = itemEntity.getPrice() * itemEntity.getQuantity();
                    
                    res.items.add(itemDTO);
                }
            }

            res.success = true;
            res.message = "Lấy chi tiết đơn hàng thành công."; // Thêm message cho rõ ràng

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
            e.printStackTrace(); // Log lỗi ra console để debug nếu cần
        }
    
        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}