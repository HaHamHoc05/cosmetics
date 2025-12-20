package cosmetic.entities;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Long roleId; // 1: ADMIN, 2: CUSTOMER
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    // --- BUSINESS LOGIC ---
    public boolean isAdmin() {
        return this.roleId != null && this.roleId == 1;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long i) { this.roleId = i; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}