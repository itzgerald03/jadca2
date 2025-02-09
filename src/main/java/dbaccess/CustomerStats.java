package dbaccess;

public class CustomerStats {
    private int customerId;
    private String customerName;
    private int totalBookings;
    private double totalBookingValue;

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public double getTotalBookingValue() {
        return totalBookingValue;
    }

    public void setTotalBookingValue(double totalBookingValue) {
        this.totalBookingValue = totalBookingValue;
    }

    @Override
    public String toString() {
        return "CustomerStats{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", totalBookings=" + totalBookings +
                ", totalBookingValue=" + totalBookingValue +
                '}';
    }
}
