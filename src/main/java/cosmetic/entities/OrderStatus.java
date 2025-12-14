package cosmetic.entities;

public enum OrderStatus {
	PENDING("Chờ xử lý"),
    CONFIRMED("Đã xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELED("Đã hủy");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Business rules
    public boolean canCancel() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case PENDING:
                return newStatus == CONFIRMED || newStatus == CANCELED;
            case CONFIRMED:
                return newStatus == SHIPPING || newStatus == CANCELED;
            case SHIPPING:
                return newStatus == DELIVERED;
            case DELIVERED:
            case CANCELED:
                return false; // Trạng thái kết thúc
            default:
                return false;
        }
    }

    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELED;
    }
}
