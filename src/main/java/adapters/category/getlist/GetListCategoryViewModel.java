package adapters.category.getlist;

import adapters.Publisher;
import adapters.Subscriber;
import cosmetic.entities.Category;
import desktop.GUIAdminProduct;

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

	    public void addSubscriber(Subscriber s) { subscribe(s); 
	    }

}