package cosmetic.usecase.admin.updatestatus;

public class UpdateOrderOutputData {
	private boolean success;
    private String message;
    private String newStatus;

    public UpdateOrderOutputData(boolean success, String message, String newStatus) {
        this.success = success;
        this.message = message;
        this.newStatus = newStatus;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getNewStatus() { return newStatus; }
}