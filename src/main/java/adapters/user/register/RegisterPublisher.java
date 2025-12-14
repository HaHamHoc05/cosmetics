package adapters.user.register;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.user.register.RegisterResponse;
import desktop.Subscriber;

public class RegisterPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(RegisterResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}