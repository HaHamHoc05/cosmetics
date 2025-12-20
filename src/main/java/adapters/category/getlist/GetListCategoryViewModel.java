package adapters.category.getlist;

import adapters.Publisher;
import cosmetic.entities.Category;
import java.util.List;

public class GetListCategoryViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public List<Category> categories;

    public void setState(boolean isSuccess, String message, List<Category> categories) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.categories = categories;
        notifySubscribers();
    }
}