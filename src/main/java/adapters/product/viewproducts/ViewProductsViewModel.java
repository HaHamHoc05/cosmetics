package adapters.product.viewproducts;

import java.util.List;

import cosmetic.entities.Product;

public class ViewProductsViewModel {
	private List<Product> products;
    public void setProducts(List<Product> products){ this.products = products; }
    public List<Product> getProducts(){ return products; }
}