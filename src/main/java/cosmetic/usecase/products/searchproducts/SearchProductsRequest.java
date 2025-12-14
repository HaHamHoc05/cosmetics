package cosmetic.usecase.products.searchproducts;

import cosmetic.RequestData;

public class SearchProductsRequest extends RequestData{
	private final String keyword;
    private final Long categoryId;

    public SearchProductsRequest(String keyword, Long categoryId) {
        this.keyword = keyword;
        this.categoryId = categoryId;
    }

    public String getKeyword() { return keyword; }
    public Long getCategoryId() { return categoryId; }
}