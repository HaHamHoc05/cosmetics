package cosmetic.entities;

import java.util.regex.Pattern;

import jakarta.validation.ValidationException;

public class User {
	private static final Pattern EMAIL_PATTERN = 
	        Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	
	private Long id;
	private String username;
	private String password;
	private String email;
    private String role; // "USER" hoặc "ADMIN"

	
	public User(String username, String password, String email, String role) throws ValidationException {
		this.id = id;
		validateUsername(username);
        validatePassword(password);
        validateEmail(email);
        this.role = role != null ? role : "USER";

        
        this.username = username;
        this.password = password;
        this.email = email;
    }
	public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }


    public void setId(Long id) { this.id = id; }
    
    public void setUsername(String username) throws ValidationException {
        validateUsername(username);
        this.username = username;
    }

    public void setPassword(String password) throws ValidationException {
        validatePassword(password);
        this.password = password;
    }

    public void setEmail(String email) throws ValidationException {
        validateEmail(email);
        this.email = email;
    }

    // Private validation methods
    private void validateUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username không được để trống");
        if (username.length() < 3 || username.length() > 32)
            throw new IllegalArgumentException("Username phải từ 3 đến 32 ký tự");
        if (!username.matches("^[A-Za-z0-9._-]+$"))
            throw new IllegalArgumentException("Username chỉ được gồm chữ, số và . _ -");
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("Mật khẩu phải từ 8 ký tự trở lên");
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.matches(".*[^A-Za-z0-9].*");
        if (!(hasUpper && hasLower && hasDigit && hasSpecial))
            throw new IllegalArgumentException("Mật khẩu cần có chữ hoa, chữ thường, số và ký tự đặc biệt");
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email không được để trống");
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("Email không hợp lệ");
    }

    // Business methods
    public boolean checkPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    } 
}
