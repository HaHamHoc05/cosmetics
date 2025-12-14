package adapters.user.register;

import cosmetic.entities.User;

public class RegisterViewModel {
	 private final Long id;
	    private final String username;
	    private final String email;

	    public RegisterViewModel(User user){
	        this.id = user.getId();
	        this.username = user.getUsername();
	        this.email = user.getEmail();
	    }

	    public Long getId(){ return id; }
	    public String getUsername(){ return username; }
	    public String getEmail(){ return email; }
	}