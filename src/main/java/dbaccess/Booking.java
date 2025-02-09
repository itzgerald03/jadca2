package dbaccess;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class Booking {
    private int id;
    private int memberId;
    private String memberName;
    private Timestamp bookingDate;
    private Timestamp appointmentDate;
    private String specialRequests;
    private String status;
    private List<BookingDetail> bookingDetails;
    private String address;
    private double totalPrice; 

    // Default constructor
    public Booking() {
        this.bookingDetails = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }

    public Timestamp getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Timestamp appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BookingDetail> getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(List<BookingDetail> bookingDetails) { this.bookingDetails = bookingDetails; }

    public double getTotalAmount() {
        return bookingDetails.stream()
                             .mapToDouble(BookingDetail::getTotalPrice)
                             .sum();
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // New getter and setter for totalPrice
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
