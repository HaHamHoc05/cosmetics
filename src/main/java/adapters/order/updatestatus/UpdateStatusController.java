package adapters.order.updatestatus;

import cosmetic.entities.OrderStatus;
import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.order.updatestatus.UpdateStatusReq;
import cosmetic.usecase.order.updatestatus.UpdateStatusRes;

public class UpdateStatusController {
    private final InputBoundary<UpdateStatusReq, UpdateStatusRes> inputBoundary;

    public UpdateStatusController(InputBoundary<UpdateStatusReq, UpdateStatusRes> inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    // Input nhận String từ ComboBox/Dropdown trên giao diện
    public void execute(Long orderId, String newStatusString) {
        try {
            // Chuyển String (VD: "DELIVERED") sang Enum
            OrderStatus status = OrderStatus.valueOf(newStatusString);
            
            UpdateStatusReq req = new UpdateStatusReq(orderId, status);
            inputBoundary.execute(req);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Trạng thái không hợp lệ!");
        }
    }
}