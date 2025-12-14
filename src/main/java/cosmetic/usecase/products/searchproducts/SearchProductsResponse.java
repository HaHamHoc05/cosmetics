package cosmetic.usecase.products.searchproducts;

import java.util.List;

import cosmetic.ResponseData;
import cosmetic.entities.Product;

public class SearchProductsResponse extends ResponseData {
    private final List<Product> products;

    public SearchProductsResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() { return products; }
}
