package cosmetic.repository;

import java.util.List;

import cosmetic.entities.User;

public interface UserRepository {
	User findById(Long id);
    User findByUsername(String username);
    void save(User user);
    void update(User user);
    void delete(Long id);
    List<User> findAll();
}

