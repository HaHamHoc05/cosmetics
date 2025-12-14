package adapters.user.login;

import cosmetic.entities.User;

public class LoginViewModel {
	private final Long id;
    private final String username;
    private final String role;

    public LoginViewModel(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.isAdmin() ? "ADMIN" : "USER";
    }

    public Long getId(){ return id; }
    public String getUsername(){ return username; }
    public String getRole(){ return role; }
}