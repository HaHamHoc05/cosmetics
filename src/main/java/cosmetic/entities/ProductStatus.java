package cosmetic.entities;

public enum ProductStatus {
	AVAILABLE("Đang kinh doanh"),
    OUT_OF_STOCK("Hết hàng"),;

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Business rules
    public boolean canSell() {
        return this == AVAILABLE;
    }

}