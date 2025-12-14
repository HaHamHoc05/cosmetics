package adapters.order.checkout;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.order.checkout.CheckoutResponse;
import desktop.Subscriber;

public class CheckoutPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(CheckoutResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}
