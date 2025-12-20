package cosmetic.repository;

import cosmetic.entities.User;

public interface UserRepository {

    void save(User user);
    
    User findByEmail(String email);
    
    User findById(Long id);
    
    User findByUsername(String username);

}
