package adapters.order.vieworder;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.order.vieworders.ViewOrdersResponse;
import desktop.Subscriber;

public class ViewOrdersPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(ViewOrdersResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}