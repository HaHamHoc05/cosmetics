package adapters.order.vieworderdetail;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.order.viewordersdetail.ViewOrderDetailResponse;
import desktop.Subscriber;

public class ViewOrderDetailPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(ViewOrderDetailResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}