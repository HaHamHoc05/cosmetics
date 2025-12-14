package adapters.cart.addtocart;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.cart.addtocart.AddToCartResponse;
import desktop.Subscriber;

public class AddToCartPublisher {
	private List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(AddToCartResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}
