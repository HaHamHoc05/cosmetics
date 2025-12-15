package cosmetic.usecase.admin.updatestatus;

public class UpdateOrderInputData {
	private Long requesterId;
	private Long orderId;
	private String action; // "status"

	public UpdateOrderInputData(Long requesterId, Long orderId, String action) {
        this.requesterId = requesterId;
        this.orderId = orderId;
        this.action = action;
    }
	public Long getRequesterId() { return requesterId; }
    public Long getOrderId() { return orderId; }
    public String getAction() { return action; }
}
