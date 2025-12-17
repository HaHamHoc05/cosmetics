package cosmetic.usecase.order.create;

import cosmetic.entities.Cart;
import cosmetic.entities.Order;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class CreateOrderUseCase implements UseCase<CreateOrderReq, CreateOrderRes> {

    private final OrderRepository repository;
    
    private final OutputBoundary< CreateOrderRes> outputBoundary;

    // Constructor
    public CreateOrderUseCase(OrderRepository repository, OutputBoundary< CreateOrderRes> outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateOrderReq req) {
        CreateOrderRes res = new CreateOrderRes();

        try {
            // INPUT VALIDATION 
        	if (req.userId == null) {
                throw new IllegalArgumentException("User ID không được để trống.");
            }
            if (req.address == null || req.address.trim().isEmpty()) {
                throw new IllegalArgumentException("Địa chỉ giao hàng không được để trống.");
            }
            if (req.phone == null || !req.phone.matches("0[35789][0-9]{8}")) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
            }

            // USER VALIDATION 
            User user = repository.findUserById(req.userId);
            if (user == null) {
                throw new RuntimeException("Người dùng không tồn tại.");
            }

            //  CART VALIDATION 
            Cart cart = repository.findCartByUserId(req.userId);
            if (cart == null || cart.getItems().isEmpty()) {
                throw new RuntimeException("Giỏ hàng trống, vui lòng chọn sản phẩm.");
            }

         // Gọi Entity xử lý nghiệp vụ lõi
            Order newOrder = Order.createFromCart(cart, user, req.address, req.phone, req.paymentMethod);

            repository.save(newOrder);
            repository.deleteCart(req.userId);

            res.success = true;
            res.newOrderId = newOrder.getId();
            res.message = "Đặt hàng thành công!";

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}