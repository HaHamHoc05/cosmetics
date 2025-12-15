package cosmetic.repository;

import cosmetic.entities.User;

public interface UserRepository {
    void save(User user);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    
    // Username hoặc Email
    User findByUsernameOrEmail(String identifier);
}