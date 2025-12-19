package usecase;

import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.entities.Order;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.create.CreateOrderReq;
import cosmetic.usecase.order.create.CreateOrderRes;
import cosmetic.usecase.order.create.CreateOrderUseCase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestCreateOrderUseCase {

    // 1. Tạo Repository Giả (Mock thủ công)
    class MockOrderRepository implements OrderRepository {
        public boolean saveCalled = false; // Cờ để kiểm tra hàm save có được gọi không
        
        @Override
        public User findUserById(Long userId) {
            if (userId == 1L) return new User(); // Giả vờ tìm thấy user 1
            return null; // Các user khác không thấy
        }

        @Override
        public Cart findCartByUserId(Long userId) {
            Cart cart = new Cart();
            // Giả vờ giỏ hàng có 1 sản phẩm
            List<CartItem> items = new ArrayList<>();
            CartItem item = new CartItem();
            item.setProductId(100);
            item.setPrice(50000);
            item.setQuantity(2);
            items.add(item);
            cart.setItems(items);
            return cart;
        }

        @Override
        public void save(Order order) {
            saveCalled = true; // Đánh dấu là đã gọi hàm save
        }

        @Override public Order findById(Long id) { return null; }
        @Override public List<Order> findByUserId(Long userId) { return null; }
        @Override public List<Order> findAll() { return null; }
        @Override public void deleteCart(Long userId) {}
    }

    // 2. Tạo OutputBoundary Giả (Presenter Giả)
    class MockPresenter implements OutputBoundary<CreateOrderRes> {
        public CreateOrderRes capturedRes; // Hứng kết quả để soi

        @Override
        public void present(CreateOrderRes response) {
            this.capturedRes = response;
        }
    }

    @Test
    public void testCreateOrder_ThanhCong() {
        // --- CHUẨN BỊ (ARRANGE) ---
        MockOrderRepository mockRepo = new MockOrderRepository();
        MockPresenter mockPresenter = new MockPresenter();
        CreateOrderUseCase useCase = new CreateOrderUseCase(mockRepo, mockPresenter);

        CreateOrderReq req = new CreateOrderReq();
        req.userId = 1L;
        req.address = "Sài Gòn";
        req.phone = "0987654321"; // Regex đúng
        req.paymentMethod = "COD";

        // --- HÀNH ĐỘNG (ACT) ---
        useCase.execute(req);

        // --- KIỂM TRA (ASSERT) ---
        // 1. Kiểm tra kết quả trả về có thành công không
        assertNotNull(mockPresenter.capturedRes);
        assertTrue(mockPresenter.capturedRes.success);
        assertEquals("Đặt hàng thành công!", mockPresenter.capturedRes.message);

        // 2. Kiểm tra xem Repository có được gọi hàm save không
        assertTrue(mockRepo.saveCalled, "Hàm save của Repository phải được gọi");
    }

    @Test
    public void testCreateOrder_ThatBai_DoSoDienThoaiSai() {
        MockOrderRepository mockRepo = new MockOrderRepository();
        MockPresenter mockPresenter = new MockPresenter();
        CreateOrderUseCase useCase = new CreateOrderUseCase(mockRepo, mockPresenter);

        CreateOrderReq req = new CreateOrderReq();
        req.userId = 1L;
        req.address = "Sài Gòn";
        req.phone = "abc"; // Sai định dạng

        useCase.execute(req);

        // Kỳ vọng thất bại
        assertFalse(mockPresenter.capturedRes.success);
        assertEquals("Số điện thoại không hợp lệ.", mockPresenter.capturedRes.message);
        // Kỳ vọng hàm save KHÔNG được gọi
        assertFalse(mockRepo.saveCalled);
    }
}