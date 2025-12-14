package cosmetic.entities;

public class Category {
    private Long id;
    private String name;
    private String description;

    public Category(String name, String description) {
        validateName(name);
        this.name = name;
        this.description = description;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    // Validation - JAVA 8 COMPATIBLE
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Tên loại sản phẩm không được để trống");
        if (name.length() < 2 || name.length() > 100)
            throw new IllegalArgumentException("Tên loại sản phẩm phải từ 2 đến 100 ký tự");
    }
}