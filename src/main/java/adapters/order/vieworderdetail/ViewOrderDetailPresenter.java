package adapters.order.vieworderdetail;

import cosmetic.OutputBoundary;
import cosmetic.entities.Order;
import cosmetic.entities.OrderItem;
import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailResponse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ViewOrderDetailPresenter implements OutputBoundary<ViewOrderDetailResponse> {
    private final ViewOrderDetailViewModel viewModel;

    public ViewOrderDetailPresenter() {
        this.viewModel = new ViewOrderDetailViewModel();
    }

    public ViewOrderDetailViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void present(ViewOrderDetailResponse response) {
        Order order = response.getOrder();
        
        // 1. Format thông tin chung đơn hàng
        viewModel.setOrderId(String.valueOf(order.getId()));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        viewModel.setOrderDate(order.getCreatedAt().format(formatter));
        
        viewModel.setStatus(order.getStatus().getDisplayName());
        viewModel.setTotalAmount(String.format("%,.0f đ", order.getTotalAmount()));
        viewModel.setPaymentMethod(order.getPaymentMethodDisplay()); // Hàm này đã có trong Entity Order
        viewModel.setShippingAddress(order.getShippingAddress());
        
        // 2. Format danh sách sản phẩm (Items)
        List<ViewOrderDetailViewModel.Item> viewItems = new ArrayList<>();
        
        for (OrderItem item : order.getItems()) {
            String productName = "Sản phẩm không xác định";
            if (item.getProduct() != null) {
                productName = item.getProduct().getName();
            } else {
                productName = "Sản phẩm đã bị xóa (ID: " + item.getProduct().getId() + ")";
            }

            // QUAN TRỌNG: Lấy giá từ item.getPrice() (giá lịch sử)
            String priceStr = String.format("%,.0f đ", item.getPrice());
            String subtotalStr = String.format("%,.0f đ", item.getSubtotal());

            viewItems.add(new ViewOrderDetailViewModel.Item(
                productName,
                priceStr,
                item.getQuantity(),
                subtotalStr
            ));
        }
        
        viewModel.setItems(viewItems);
    }
}