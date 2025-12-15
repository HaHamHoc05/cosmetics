package cosmetic.usecase.order.checkout;

import adapters.order.checkout.CheckoutPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Order;
import cosmetic.repository.CartRepository;
import cosmetic.repository.OrderRepository;
import cosmetic.repository.ProductRepository;
import cosmetic.repository.impl.DatabaseConnection; 

public class CheckoutUseCase implements UseCase<CheckoutRequest, CheckoutResponse> {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CheckoutPublisher publisher;

    public CheckoutUseCase(CartRepository cartRepo, OrderRepository orderRepo, 
                           ProductRepository productRepo, CheckoutPublisher publisher){
        this.cartRepository = cartRepo;
        this.orderRepository = orderRepo;
        this.productRepository = productRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(CheckoutRequest request, OutputBoundary<CheckoutResponse> output){
        try {
            // 1. BẮT ĐẦU TRANSACTION
            DatabaseConnection.startTransaction();

            // 2. Validate Giỏ hàng
            Cart cart = cartRepository.findByUserId(request.getUserId());
            if(cart == null || cart.isEmpty()) 
                throw new RuntimeException("Giỏ hàng trống");

            // 3. Tạo đơn hàng (Chưa lưu DB ngay, hoặc lưu nhưng chưa commit)
            Order order = Order.createFromCart(cart, request.getPaymentMethod(), request.getShippingAddress());
            orderRepository.save(order); // Lưu Order và OrderItems

            // 4. Trừ tồn kho (Xử lý Race Condition)
            for(CartItem item: cart.getItems()){
                // Gọi hàm Atomic Update đã viết ở Bước 3
                boolean success = productRepository.reduceStockAtomic(
                    item.getProduct().getId(), 
                    item.getQuantity()
                );

                if (!success) {
                    // Nếu không trừ được (do hết hàng giữa chừng), ném lỗi để Rollback toàn bộ
                    throw new RuntimeException("Sản phẩm " + item.getProduct().getName() + " vừa hết hàng hoặc không đủ số lượng!");
                }
            }

            // 5. Xóa giỏ hàng
            cart.clear();
            cartRepository.save(cart); // Hoặc cartRepository.clearCart(userId)

            // 6. COMMIT TRANSACTION (Tất cả thành công mới lưu thật)
            DatabaseConnection.commit();

            // 7. Thông báo kết quả
            CheckoutResponse response = new CheckoutResponse(order);
            output.present(response);
            publisher.notifySubscribers(response);

        } catch (Exception e) {
            // 8. ROLLBACK (Gặp bất cứ lỗi gì -> Hoàn tác như chưa hề có cuộc checkout)
            DatabaseConnection.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }
}