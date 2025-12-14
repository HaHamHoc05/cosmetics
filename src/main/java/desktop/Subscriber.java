package desktop;

public interface Subscriber<T> {
    void update(T data);
}