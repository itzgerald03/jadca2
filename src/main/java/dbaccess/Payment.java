package dbaccess;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private int bookingId;
    private String paymentIntentId;
    private double amount;
    private String status;
    private Timestamp createdAt;

    // Default constructor
    public Payment() {}

    // Constructor with parameters
    public Payment(int bookingId, String paymentIntentId, double amount, String status) {
        this.bookingId = bookingId;
        this.paymentIntentId = paymentIntentId;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}