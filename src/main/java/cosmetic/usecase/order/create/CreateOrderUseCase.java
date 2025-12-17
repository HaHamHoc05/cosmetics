package cosmetic.usecase.order.create;

import cosmetic.entities.Cart;
import cosmetic.entities.Order;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.UseCase;

public class CreateOrderUseCase implements UseCase<CreateOrderReq, CreateOrderRes> {

    private final OrderRepository repository;

    // Constructor nhận Repository (Dependency Injection)
    public CreateOrderUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreateOrderRes execute(CreateOrderReq req) {
        CreateOrderRes res = new CreateOrderRes();

        try {
            // 1. Lấy dữ liệu từ DB thông qua Repository
            Cart cart = repository.findCartByUserId(req.userId);
            User user = repository.findUserById(req.userId);

            // 2. Gọi Logic nghiệp vụ (Business Rules) nằm trong Entity Order
            // Hàm này của bạn đã lo việc trừ tồn kho và tính tiền rồi
            Order newOrder = Order.createFromCart(cart, user, req.address, req.phone, req.paymentMethod);

            // 3. Lưu đơn hàng và Xóa giỏ hàng cũ
            repository.save(newOrder);
            repository.deleteCart(req.userId);

            // 4. Báo cáo thành công
            res.success = true;
            res.newOrderId = newOrder.getId();
            res.message = "Đặt hàng thành công!";

        } catch (Exception e) {
            // 5. Xử lý lỗi (Ví dụ: Giỏ hàng rỗng, Hết hàng...)
            res.success = false;
            res.message = "Lỗi đặt hàng: " + e.getMessage();
        }

        return res;
    }
}