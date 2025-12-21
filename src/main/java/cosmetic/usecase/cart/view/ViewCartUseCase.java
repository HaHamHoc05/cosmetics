package cosmetic.usecase.cart.view;

import cosmetic.entities.Cart;
import cosmetic.entities.CartItem;
import cosmetic.repository.CartRepository;
import cosmetic.usecase.UseCase;
import java.util.ArrayList;
import java.util.List;

import adapters.cart.view.ViewCartPresenter;

public class ViewCartUseCase implements UseCase<ViewCartReq, ViewCartRes> {
    
    // Dùng interface Repository chuẩn của bạn
    private final CartRepository cartRepository;
    // Dùng Presenter cụ thể (vì bạn đang dùng mô hình MVP/Clean Arch như các bài trước)
    private final ViewCartPresenter presenter;

    public ViewCartUseCase(CartRepository cartRepository, ViewCartPresenter presenter) {
        this.cartRepository = cartRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCartReq req) {
        ViewCartRes res = new ViewCartRes();
        
        try {
            // 1. Lấy giỏ hàng từ Database (Method này có trong Repository của bạn)
            Cart cart = cartRepository.findByUserId(req.userId);

            List<CartDetailDTO> listDTO = new ArrayList<>();
            double grandTotal = 0; // Dùng double cho đồng bộ với Entity

            if (cart != null && cart.getItems() != null) {
                // 2. Duyệt qua từng CartItem (Entity) để chuyển sang DTO
                for (CartItem item : cart.getItems()) {
                    CartDetailDTO dto = new CartDetailDTO();
                    
                    // Dùng SETTER (vì biến là private)
                    dto.setProductId(item.getProductId());
                    dto.setProductName(item.getProductName());
                    dto.setPrice(item.getPrice());
                    dto.setQuantity(item.getQuantity());
                    
                    // Tính tổng tiền từng dòng
                    double lineTotal = item.getPrice() * item.getQuantity();
                    dto.setTotalPrice(lineTotal);

                    listDTO.add(dto);
                    grandTotal += lineTotal;
                }
            }

            // 3. Gán kết quả
            res.items = listDTO;
            res.grandTotal = grandTotal;

        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần
        }

        // 4. Đẩy ra Presenter
        presenter.present(res);
    }
}