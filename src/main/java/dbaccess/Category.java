package dbaccess;

public class Category {
    private int id;
    private String categoryName;

    // Default constructor
    public Category() {}

    // Constructor with parameters
    public Category(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
