package dbaccess;

public class CartItem {
    private int serviceId;
    private String serviceName;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    
    public CartItem(int serviceId, String serviceName, String description, 
                   double price, String imageUrl) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 1;
    }
    
    // Getters and setters
    public int getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImageUrl() { return imageUrl; }
    
    public double getTotal() {
        return price * quantity;
    }
}