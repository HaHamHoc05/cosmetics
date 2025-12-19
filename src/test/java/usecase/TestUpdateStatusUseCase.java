package usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import cosmetic.entities.Cart;
import cosmetic.entities.Order;
import cosmetic.entities.OrderStatus;
import cosmetic.entities.User;
import cosmetic.repository.OrderRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.updatestatus.UpdateStatusReq;
import cosmetic.usecase.order.updatestatus.UpdateStatusRes;
import cosmetic.usecase.order.updatestatus.UpdateStatusUseCase;

public class TestUpdateStatusUseCase {

    // --- 1. MOCK OBJECTS (Các diễn viên đóng thế) ---

    // Repository giả: Giả vờ tìm thấy đơn hàng để test
    class MockOrderRepository implements OrderRepository {
        public boolean saveCalled = false; // Cờ để kiểm tra xem có gọi lưu xuống DB không
        public Order orderInDb;            // Đơn hàng đang nằm trong DB giả

        // Setup đơn hàng giả vào DB
        public void setOrderInDb(Order order) {
            this.orderInDb = order;
        }

        @Override
        public Order findById(Long id) {
            // Nếu ID khớp với đơn trong DB thì trả về, không thì trả về null
            if (orderInDb != null && orderInDb.getId().equals(id)) {
                return orderInDb;
            }
            return null;
        }

        @Override
        public void save(Order order) {
            this.saveCalled = true; // Đánh dấu là đã lưu
            this.orderInDb = order; // Cập nhật lại DB giả
        }

        // Các hàm không dùng đến thì để trống
        @Override public void deleteCart(Long userId) {}
        @Override public User findUserById(Long userId) { return null; }
        @Override public Cart findCartByUserId(Long userId) { return null; }
        @Override public List<Order> findByUserId(Long userId) { return null; }
        @Override public List<Order> findAll() { return null; }
    }

    // Presenter giả: Hứng kết quả trả về
    class MockPresenter implements OutputBoundary<UpdateStatusRes> {
        public UpdateStatusRes capturedRes;

        @Override
        public void present(UpdateStatusRes response) {
            this.capturedRes = response;
        }
    }

    // --- 2. CÁC TEST CASES (Kịch bản kiểm thử) ---

    @Test
    public void testUpdateStatus_ThanhCong() {
        // [ARRANGE] Chuẩn bị dữ liệu
        MockOrderRepository mockRepo = new MockOrderRepository();
        MockPresenter mockPresenter = new MockPresenter();
        UpdateStatusUseCase useCase = new UpdateStatusUseCase(mockRepo, mockPresenter);

        // Tạo một đơn hàng giả đang ở trạng thái PENDING
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        mockRepo.setOrderInDb(order); // Nhét vào DB giả

        // Tạo request muốn đổi sang DELIVERED
        UpdateStatusReq req = new UpdateStatusReq(1L, OrderStatus.DELIVERED);

        // [ACT] Thực thi
        useCase.execute(req);

        // [ASSERT] Kiểm tra kết quả
        assertNotNull(mockPresenter.capturedRes);
        assertTrue(mockPresenter.capturedRes.success, "Phải thành công");
        assertEquals(OrderStatus.DELIVERED, mockRepo.orderInDb.getStatus(), "Trạng thái trong DB phải đổi thành DELIVERED");
        assertTrue(mockRepo.saveCalled, "Phải gọi hàm save để lưu lại");
    }

    @Test
    public void testUpdateStatus_ThatBai_DoDonDaHuy() {
        // [ARRANGE]
        MockOrderRepository mockRepo = new MockOrderRepository();
        MockPresenter mockPresenter = new MockPresenter();
        UpdateStatusUseCase useCase = new UpdateStatusUseCase(mockRepo, mockPresenter);

        // Tạo đơn hàng giả đã bị CANCELLED
        Order order = new Order();
        order.setId(2L);
        order.setStatus(OrderStatus.CANCELLED); 
        mockRepo.setOrderInDb(order);

        // Cố tình muốn đổi sang DELIVERED (Giao hàng cho đơn đã hủy??)
        UpdateStatusReq req = new UpdateStatusReq(2L, OrderStatus.DELIVERED);

        // [ACT]
        useCase.execute(req);

        // [ASSERT]
        assertFalse(mockPresenter.capturedRes.success, "Phải thất bại");
        assertTrue(mockPresenter.capturedRes.message.contains("đã hủy"), "Thông báo lỗi phải đúng");
        assertFalse(mockRepo.saveCalled, "Không được phép lưu xuống DB");
    }

    @Test
    public void testUpdateStatus_ThatBai_KhongTimThayDon() {
        // [ARRANGE]
        MockOrderRepository mockRepo = new MockOrderRepository();
        MockPresenter mockPresenter = new MockPresenter();
        UpdateStatusUseCase useCase = new UpdateStatusUseCase(mockRepo, mockPresenter);

        // Không setOrderInDb gì cả -> DB trống rỗng

        UpdateStatusReq req = new UpdateStatusReq(999L, OrderStatus.DELIVERED);

        // [ACT]
        useCase.execute(req);

        // [ASSERT]
        assertFalse(mockPresenter.capturedRes.success);
        assertTrue(mockPresenter.capturedRes.message.contains("Không tìm thấy"), "Phải báo lỗi không tìm thấy");
    }
}