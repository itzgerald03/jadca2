package dbaccess;

public class Service {
    private int id;
    private int categoryId;
    private String categoryName;
    private String serviceName;
    private String description;
    private double price;
    private String imageUrl;
    private double avgRating;  
    private int bookingCount;  
    
    // Constructors
    public Service() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }


public double getAvgRating() { return avgRating; }
public void setAvgRating(double avgRating) { this.avgRating = avgRating; }

public int getBookingCount() { return bookingCount; }
public void setBookingCount(int bookingCount) { this.bookingCount = bookingCount; }
}