package cosmetic.repository.impl;

import cosmetic.entities.User;
import cosmetic.repository.UserRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public InMemoryUserRepository() {
        // Khởi tạo user mẫu
        try {
            User admin = new User("admin", "Admin@123", "admin@cosmetic.com", "ADMIN");
            admin.setId(idGenerator.getAndIncrement());
            users.put(admin.getId(), admin);
            
            User user1 = new User("user1", "User@123", "user1@test.com", "USER");
            user1.setId(idGenerator.getAndIncrement());
            users.put(user1.getId(), user1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID không được null khi update");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}