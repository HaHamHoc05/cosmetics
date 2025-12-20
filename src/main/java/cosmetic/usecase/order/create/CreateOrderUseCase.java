package cosmetic.usecase.order.create;

import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Order;
import cosmetic.entities.Product;
import cosmetic.entities.User;
import cosmetic.repository.CartRepository;
import cosmetic.repository.OrderRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class CreateOrderUseCase implements UseCase<CreateOrderReq, CreateOrderRes> {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo; // SỬA: Thêm repo sản phẩm
    private final OutputBoundary<CreateOrderRes> outputBoundary;

    // SỬA: Constructor nhận thêm ProductRepository
    public CreateOrderUseCase(OrderRepository orderRepo, 
                              CartRepository cartRepo, ProductRepository productRepo, 
                              OutputBoundary<CreateOrderRes> outputBoundary) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateOrderReq req) {
        CreateOrderRes res = new CreateOrderRes();

        try {
            // 1. Validate Input
            if (req.userId == null) throw new IllegalArgumentException("User ID trống.");
            if (req.address == null || req.address.isBlank()) throw new IllegalArgumentException("Địa chỉ trống.");
            if (req.phone == null || !req.phone.matches("0[35789][0-9]{8}")) throw new IllegalArgumentException("SĐT không hợp lệ.");

            // 2. Lấy dữ liệu
            User user = orderRepo.findUserById(req.userId);
            if (user == null) throw new RuntimeException("Người dùng không tồn tại.");

            Cart cart = orderRepo.findCartByUserId(req.userId);
            if (cart == null || cart.getItems().isEmpty()) {
                throw new RuntimeException("Giỏ hàng trống.");
            }

            // 3. Kiểm tra tồn kho (Logic quan trọng)
            for (CartItem item : cart.getItems()) {
                Product p = productRepo.findById(item.getProductId());
                if (p == null || !p.canSell(item.getQuantity())) {
                    throw new RuntimeException("Sản phẩm " + (p != null ? p.getName() : "ID:" + item.getProductId()) + " không đủ hàng.");
                }
            }

            // 4. Tạo đơn hàng (Logic trong Entity Order)
            Order newOrder = Order.createFromCart(cart, user, req.address, req.phone, req.paymentMethod);

            // 5. Lưu đơn hàng và cập nhật kho
            orderRepo.save(newOrder); 
            
            // Trừ tồn kho và cập nhật DB
            for (CartItem item : cart.getItems()) {
                Product p = productRepo.findById(item.getProductId());
                p.decreaseStock(item.getQuantity()); 
                productRepo.updateQuantity(p.getId(), p.getQuantity());
            }

            // Xóa giỏ hàng sau khi mua thành công
            orderRepo.deleteCart(req.userId);

            res.success = true;
            res.newOrderId = newOrder.getId();
            res.message = "Đặt hàng thành công!";

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}