package dbaccess;

import java.sql.Timestamp;

public class Feedback {
    private int id;
    private int bookingId;
    private int memberId;
    private String memberName;
    private String serviceName;
    private int rating;
    private String comments;
    private Timestamp feedbackDate;
    
    // Default constructor
    public Feedback() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public Timestamp getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(Timestamp feedbackDate) { this.feedbackDate = feedbackDate; }
}