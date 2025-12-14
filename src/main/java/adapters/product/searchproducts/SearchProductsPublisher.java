package adapters.product.searchproducts;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.products.searchproducts.SearchProductsResponse;
import desktop.Subscriber;

public class SearchProductsPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber sub) { subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub) { subscribers.remove(sub); }

    public void notifySubscribers(SearchProductsResponse response) {
        for (Subscriber sub : subscribers) {
            sub.update(response);
        }
    }
}