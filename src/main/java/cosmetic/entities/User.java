package cosmetic.entities;


public class User {
    private Long id;
    private String username;
    private String email;
    private String password; 
    private String fullName;
    private String phone;
    private String address;
    private Role role;

    public User(Long id, String username, String email, String password, String fullName, Role role) {
        validateUsername(username);
        validateEmail(email);
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role != null ? role : Role.CUSTOMER;
    }

    // BR

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) 
            throw new RuntimeException("Username không được để trống");
        if (username.contains(" ")) 
            throw new RuntimeException("Username không được chứa khoảng trắng");
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) 
            throw new RuntimeException("Email không hợp lệ");
    }

    //So khớp mật khẩu 
     
    public boolean matchPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }
    
    public boolean isAdmin() { return this.role == Role.ADMIN; }


    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}