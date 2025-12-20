package cosmetic.usecase.cart.view;

import cosmetic.usecase.ResponseData;
import java.math.BigDecimal;
import java.util.List;

public class ViewCartRes extends ResponseData {
    public boolean success;
    public String message;
    public List<CartDetailDTO> items;
    public BigDecimal grandTotal;
}