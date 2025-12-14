package adapters.cart.updatecart;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.cart.updatecart.UpdateCartResponse;
import desktop.Subscriber;

public class UpdateCartPublisher {
	private List<Subscriber<UpdateCartResponse>> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber<UpdateCartResponse> sub) { subscribers.add(sub); }
    public void removeSubscriber(Subscriber<UpdateCartResponse> sub) { subscribers.remove(sub); }
    public void notifySubscribers(UpdateCartResponse response) {
        for (Subscriber<UpdateCartResponse> sub : subscribers) sub.update(response);
    }
}

