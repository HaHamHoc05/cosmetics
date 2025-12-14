package cosmetic.usecase.products.viewproducts;

import java.util.List;
import cosmetic.ResponseData;
import cosmetic.entities.Product;

public class ViewProductsResponse extends ResponseData {
    private final List<Product> products;

    public ViewProductsResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}