package adapters.user.login;

import java.util.ArrayList;
import java.util.List;

import cosmetic.usecase.user.login.LoginResponse;
import desktop.Subscriber;

public class LoginPublisher {
	private final List<Subscriber> subscribers = new ArrayList<>();
    public void addSubscriber(Subscriber sub){ subscribers.add(sub); }
    public void removeSubscriber(Subscriber sub){ subscribers.remove(sub); }
    public void notifySubscribers(LoginResponse response){
        for(Subscriber sub:subscribers) sub.update(response);
    }
}