package cosmetic.usecase.category.getlist;

import cosmetic.entities.Category;
import cosmetic.usecase.ResponseData;
import java.util.List;

public class GetListCategoryRes extends ResponseData {
    public boolean success;
    public String message;
    public List<Category> categories;
}