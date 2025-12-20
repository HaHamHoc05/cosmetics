package cosmetic.usecase.cart.view;
import java.math.BigDecimal;

public class CartDetailDTO {
    public Long productId;
    public String productName;
    public String image;
    public BigDecimal price;
    public int quantity;
    public BigDecimal totalPrice;
}