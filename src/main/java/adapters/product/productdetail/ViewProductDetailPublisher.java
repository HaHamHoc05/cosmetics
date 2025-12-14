package adapters.product.productdetail;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.products.productdetail.ViewProductDetailResponse;
import desktop.Subscriber;

public class ViewProductDetailPublisher {
	 private List<Subscriber> subscribers = new ArrayList<>();
	    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
	    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
	    public void notifySubscribers(ViewProductDetailResponse response){
	        for(Subscriber sub:subscribers) sub.update(response);
	    }
	}