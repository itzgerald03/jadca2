<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*,
                 dbaccess.DBConnection,
                 dbaccess.CategoryDAO,
                 dbaccess.Category,
                 dbaccess.ServiceDAO,
                 dbaccess.Service,
                 dbaccess.UserDAO,
                 dbaccess.User,
                 dbaccess.BookingDAO,
                 dbaccess.Booking,
                 dbaccess.BookingDetail,
                 dbaccess.FeedbackDAO,
                 dbaccess.Feedback,
                 java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <!-- Note the use of the dynamic context path here -->
    <link rel="stylesheet" href="css/adminPage.css">
</head>
<body>
    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <%
        // Check if user is logged in and is an admin
        if (session.getAttribute("userRole") == null || 
            !"Admin".equalsIgnoreCase((String)session.getAttribute("userRole"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Initialize CategoryDAO once for the entire page
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> allCategories = null;
        try {
            allCategories = categoryDAO.getAllCategories();
        } catch (Exception e) {
            out.println("<p class='error'>Error initializing categories: " + e.getMessage() + "</p>");
        }
    %>

    <h1>Admin Dashboard</h1>

    <!-- Tab Navigation -->
    <div class="tabs">
        <button class="tablinks" onclick="openTab(event, 'serviceCategories')">Service Categories</button>
        <button class="tablinks" onclick="openTab(event, 'services')">Services</button>
        <button class="tablinks" onclick="openTab(event, 'members')">Members</button>
        <button class="tablinks" onclick="openTab(event, 'bookings')">Bookings</button>
        <button class="tablinks" onclick="openTab(event, 'feedback')">Feedback</button>
    </div>

    <!-- Service Categories Tab -->
    <div id="serviceCategories" class="tabcontent">
        <h2>Manage Service Categories</h2>
        
        <!-- Display Messages if any -->
        <% if (session.getAttribute("error") != null) { %>
            <p class="error"><%= session.getAttribute("error") %></p>
            <% session.removeAttribute("error"); %>
        <% } %>
        <% if (session.getAttribute("success") != null) { %>
            <p class="success"><%= session.getAttribute("success") %></p>
            <% session.removeAttribute("success"); %>
        <% } %>

        <form action="${pageContext.request.contextPath}/CreateCategoryServlet" method="post">
            <div>
                <label for="categoryName">Category Name</label>
                <input type="text" name="categoryName" required>
            </div>
            <div>
                <button type="submit">Create Category</button>
            </div>
        </form>
        
        <h3>Existing Categories</h3>
        <!-- Filter Controls (added from the 2nd file) -->
        <div class="filter-controls">
            <input type="text" id="serviceSearch" placeholder="Search services..." onkeyup="filterServices()">
            <button type="button" onclick="filterServicesBy('bestRated')">Best Rated</button>
            <button type="button" onclick="filterServicesBy('lowestRated')">Lowest Rated</button>
            <button type="button" onclick="filterServicesBy('highDemand')">High Demand</button>
        </div>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if (allCategories != null) {
                        for (Category category : allCategories) {
                %>
                <tr>
                    <td><%= category.getId() %></td>
                    <td>
                        <span id="display_<%= category.getId() %>">
                            <%= category.getCategoryName() %>
                        </span>
                        <form id="form_<%= category.getId() %>" 
                              action="${pageContext.request.contextPath}/UpdateCategoryServlet" 
                              method="post" 
                              style="display: none;">
                            <input type="hidden" name="id" value="<%= category.getId() %>">
                            <input type="text" name="categoryName" value="<%= category.getCategoryName() %>" required>
                            <button type="submit">Save</button>
                            <button type="button" onclick="toggleEdit(<%= category.getId() %>)">Cancel</button>
                        </form>
                    </td>
                    <td>
                        <a href="#" onclick="toggleEdit(<%= category.getId() %>)">Edit</a> | 
                        <a href="${pageContext.request.contextPath}/DeleteCategoryServlet?id=<%= category.getId() %>" 
                           onclick="return confirm('Are you sure you want to delete this category?')">Delete</a>
                    </td>
                </tr>
                <% 
                        }
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Services Tab -->
    <div id="services" class="tabcontent">
        <h2>Manage Services</h2>
        
        <form action="${pageContext.request.contextPath}/CreateServiceServlet" method="post" enctype="multipart/form-data">
            <div>
                <label for="serviceName">Service Name</label>
                <input type="text" name="serviceName" required>
            </div>
            <div>
                <label for="description">Description</label>
                <textarea name="description" required></textarea>
            </div>
            <div>
                <label for="price">Price</label>
                <input type="number" name="price" step="0.01" required>
            </div>
            <div>
                <label for="serviceImage">Service Image</label>
                <input type="file" name="serviceImage" accept="image/*" required>
            </div>
            <div>
                <label for="categoryId">Category</label>
                <select name="categoryId" required>
                    <% if (allCategories != null) {
                        for (Category category : allCategories) {
                    %>
                    <option value="<%= category.getId() %>"><%= category.getCategoryName() %></option>
                    <% 
                        }
                    } %>
                </select>
            </div>
            <div>
                <button type="submit">Create Service</button>
            </div>
        </form>

        <h3>Existing Services</h3>
   <table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Category</th>
            <th>Image</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <%
            ServiceDAO serviceDAO = new ServiceDAO();
            try {
                List<Service> services = serviceDAO.getAllServices();
                for (Service service : services) {
                    String imageUrl = request.getContextPath() + "/CA1/" + service.getImageUrl().replace("\\", "/");
        %>
        <tr id="display_row_<%= service.getId() %>">
            <td><%= service.getId() %></td>
            <td><%= service.getServiceName() %></td>
            <td><%= service.getDescription() %></td>
            <td>$<%= String.format("%.2f", service.getPrice()) %></td>
            <td><%= service.getCategoryName() %></td>
            <td>
                <img src="<%= imageUrl %>" alt="Service Image" style="width: 100px; height: auto;">
            </td>
            <td>
                <a href="#" onclick="toggleEditService(<%= service.getId() %>)">Edit</a> | 
                <a href="${pageContext.request.contextPath}/DeleteServiceServlet?id=<%= service.getId() %>" 
                   onclick="return confirm('Are you sure you want to delete this service?')">Delete</a>
            </td>
        </tr>
        <tr id="edit_row_<%= service.getId() %>" style="display: none;">
            <td><%= service.getId() %></td>
            <td colspan="6">
                <form id="editForm_<%= service.getId() %>" 
                      action="${pageContext.request.contextPath}/UpdateServiceServlet" 
                      method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="<%= service.getId() %>">
                    
                    <label for="serviceName">Service Name:</label>
                    <input type="text" name="serviceName" value="<%= service.getServiceName() %>" required>
                    
                    <label for="description">Description:</label>
                    <textarea name="description" required><%= service.getDescription() %></textarea>
                    
                    <label for="price">Price:</label>
                    <input type="number" name="price" step="0.01" value="<%= service.getPrice() %>" required>
                    
                    <label for="categoryId">Category:</label>
                    <select name="categoryId" required>
                        <% if (allCategories != null) {
                            for (Category category : allCategories) { %>
                        <option value="<%= category.getId() %>" <%= category.getId() == service.getCategoryId() ? "selected" : "" %>>
                            <%= category.getCategoryName() %>
                        </option>
                        <%  }
                        } %>
                    </select>
                    
                    <label for="serviceImage">Service Image:</label>
                    <img src="<%= imageUrl %>" alt="Current Image" style="width: 50px; height: auto;">
                    <input type="file" name="serviceImage" accept="image/*">
                    <small>(Leave empty to keep current image)</small>
                    
                    <div>
                        <button type="submit">Save</button>
                        <button type="button" onclick="toggleEditService(<%= service.getId() %>)">Cancel</button>
                    </div>
                </form>
            </td>
        </tr>
        <%
                }
            } catch (Exception e) {
                out.println("<tr><td colspan='7' class='error'>Error fetching services: " + e.getMessage() + "</td></tr>");
            }
        %>
    </tbody>
</table>

    </div>

    <!-- Members Tab -->
    <div id="members" class="tabcontent">
        <h2>Manage Members</h2>
        
        <!-- Display Messages -->
        <% if (session.getAttribute("error") != null) { %>
            <p class="error"><%= session.getAttribute("error") %></p>
            <% session.removeAttribute("error"); %>
        <% } %>
        <% if (session.getAttribute("success") != null) { %>
            <p class="success"><%= session.getAttribute("success") %></p>
            <% session.removeAttribute("success"); %>
        <% } %>

        <h3>Existing Members</h3>
        <!-- Filter Controls (added from the 2nd file) -->
        <div class="filter-controls">
            <input type="text" id="nameSearch" placeholder="Search by Name" onkeyup="filterUsersByName()">
            <button type="button" onclick="filterUsersByPostal()">Sort by Postal Code</button>
        </div>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>NAME</th>
                    <th>EMAIL</th>
                    <th>PHONE</th>
                    <th>ADDRESS</th>
                    <th>ROLE</th>
                    <th>ACTIONS</th>
                </tr>
            </thead>
            <tbody>
                <%
                    UserDAO userDAO = new UserDAO();
                    try {
                        List<User> users = userDAO.getAllUsers();
                        for (User user : users) {
                %>
                <tr>
                    <td><%= user.getId() %></td>
                    <td><%= user.getName() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getPhone() %></td>
                    <td><%= user.getAddress() %></td>
                    <td><%= user.getRole() %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/DeleteMemberServlet?id=<%= user.getId() %>" 
                           onclick="return confirm('Are you sure you want to delete this member?')">Delete</a>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        out.println("<tr><td colspan='7' class='error'>Error fetching members: " + e.getMessage() + "</td></tr>");
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Bookings Tab -->
    <div id="bookings" class="tabcontent">
        <h2>Manage Bookings</h2>
        
        <!-- Display Messages -->
        <% if (session.getAttribute("error") != null) { %>
            <p class="error"><%= session.getAttribute("error") %></p>
            <% session.removeAttribute("error"); %>
        <% } %>
        <% if (session.getAttribute("success") != null) { %>
            <p class="success"><%= session.getAttribute("success") %></p>
            <% session.removeAttribute("success"); %>
        <% } %>

        <h3>Existing Bookings</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Member Name</th>
                    <th>Booking Date</th>
                    <th>Appointment Date</th>
                    <th>Services</th>
                    <th>Total Amount</th>
                    <th>Special Requests</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    BookingDAO bookingDAO = new BookingDAO();
                    try {
                        List<Booking> bookings = bookingDAO.getAllBookings();
                        for (Booking booking : bookings) {
                %>
                <tr id="booking-display-<%= booking.getId() %>">
                    <td><%= booking.getId() %></td>
                    <td><%= booking.getMemberName() %></td>
                    <td><%= booking.getBookingDate() %></td>
                    <td><%= booking.getAppointmentDate() %></td>
                    <td>
                        <% for (BookingDetail detail : booking.getBookingDetails()) { %>
                            <%= detail.getServiceName() %> (x<%= detail.getQuantity() %>)<br>
                        <% } %>
                    </td>
                    <td>$<%= String.format("%.2f", booking.getTotalAmount()) %></td>
                    <td><%= booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "" %></td>
                    <td><%= booking.getStatus() %></td>
                    <td>
                        <a href="#" onclick="toggleBookingEdit(<%= booking.getId() %>)">Edit</a> | 
                        <a href="${pageContext.request.contextPath}/DeleteBookingServlet?id=<%= booking.getId() %>" 
                           onclick="return confirm('Are you sure you want to delete this booking?')">Delete</a>
                    </td>
                </tr>
                <tr id="booking-edit-<%= booking.getId() %>" style="display: none;">
                    <td colspan="9">
                        <form action="${pageContext.request.contextPath}/UpdateBookingServlet" method="post">
                            <input type="hidden" name="id" value="<%= booking.getId() %>">
                            
                            <div>
                                <label>Appointment Date:</label>
                                <input type="datetime-local" name="appointmentDate" 
                                       value="<%= booking.getAppointmentDate().toString().replace(" ", "T").substring(0, 16) %>" 
                                       required>
                            </div>

                            <div>
                                <label>Special Requests:</label>
                                <textarea name="specialRequests"><%= booking.getSpecialRequests() != null ? booking.getSpecialRequests() : "" %></textarea>
                            </div>

                            <div>
                                <label>Status:</label>
                                <select name="status" required>
                                    <option value="Pending" <%= "Pending".equals(booking.getStatus()) ? "selected" : "" %>>Pending</option>
                                    <option value="Confirmed" <%= "Confirmed".equals(booking.getStatus()) ? "selected" : "" %>>Confirmed</option>
                                    <option value="Completed" <%= "Completed".equals(booking.getStatus()) ? "selected" : "" %>>Completed</option>
                                    <option value="Cancelled" <%= "Cancelled".equals(booking.getStatus()) ? "selected" : "" %>>Cancelled</option>
                                </select>
                            </div>

                            <div>
                                <button type="submit">Save</button>
                                <button type="button" onclick="toggleBookingEdit(<%= booking.getId() %>)">Cancel</button>
                            </div>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        out.println("<tr><td colspan='9' class='error'>Error fetching bookings: " + e.getMessage() + "</td></tr>");
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Feedback Tab -->
    <div id="feedback" class="tabcontent">
        <h2>Feedback</h2>
        
        <!-- Display Messages -->
        <% if (session.getAttribute("error") != null) { %>
            <p class="error"><%= session.getAttribute("error") %></p>
            <% session.removeAttribute("error"); %>
        <% } %>
        <% if (session.getAttribute("success") != null) { %>
            <p class="success"><%= session.getAttribute("success") %></p>
            <% session.removeAttribute("success"); %>
        <% } %>
        
        <h3>All Feedback</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Member Name</th>
                    <th>Service</th>
                    <th>Rating</th>
                    <th>Comments</th>
                    <th>Feedback Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    FeedbackDAO feedbackDAO = new FeedbackDAO();
                    try {
                        List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
                        for (Feedback feedback : feedbackList) {
                %>
                <tr>
                    <td><%= feedback.getId() %></td>
                    <td><%= feedback.getMemberName() %></td>
                    <td><%= feedback.getServiceName() %></td>
                    <td><%= feedback.getRating() %></td>
                    <td><%= feedback.getComments() %></td>
                    <td><%= feedback.getFeedbackDate() %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/DeleteFeedbackServlet?id=<%= feedback.getId() %>" 
                           onclick="return confirm('Are you sure you want to delete this feedback?')">Delete</a>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        out.println("<tr><td colspan='7' class='error'>Error fetching feedback: " + e.getMessage() + "</td></tr>");
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Include Footer -->
    <%@ include file="footer.jsp" %>

<script>
    // -------------------------------
    // Basic Tab & Edit Toggle Functions
    // -------------------------------
    function openTab(evt, tabName) {
        var i, tabcontent, tablinks;
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className += " active";
    }

    function toggleEdit(id) {
        const displayElement = document.getElementById('display_' + id);
        const formElement = document.getElementById('form_' + id);
        if (displayElement && formElement) {
            if (displayElement.style.display !== 'none') {
                displayElement.style.display = 'none';
                formElement.style.display = 'inline';
            } else {
                displayElement.style.display = 'inline';
                formElement.style.display = 'none';
            }
        }
    }

    function toggleEditService(id) {
        const displayRow = document.getElementById('display_row_' + id);
        const editRow = document.getElementById('edit_row_' + id);
        if (displayRow && editRow) {
            if (displayRow.style.display !== 'none') {
                displayRow.style.display = 'none';
                editRow.style.display = 'table-row';
            } else {
                displayRow.style.display = 'table-row';
                editRow.style.display = 'none';
            }
        }
    }

    function toggleBookingEdit(id) {
        const displayRow = document.getElementById('booking-display-' + id);
        const editRow = document.getElementById('booking-edit-' + id);
        if (displayRow && editRow) {
            if (editRow.style.display === 'none') {
                displayRow.style.display = 'none';
                editRow.style.display = 'table-row';
            } else {
                displayRow.style.display = 'table-row';
                editRow.style.display = 'none';
            }
        }
    }

    // -------------------------------
    // Filter Functions (Services & Users)
    // -------------------------------
    // Service filtering based on search input and buttons
    function filterServices() {
        let input = document.getElementById("serviceSearch").value.toLowerCase();
        let table = document.querySelector("#services table tbody");
        let rows = Array.from(table.getElementsByTagName("tr"));
        // Exclude edit rows (which have IDs starting with "edit_row_")
        let serviceRows = rows.filter(row => row.id.startsWith("display_row_"));
        let sortedRows = serviceRows.map(row => {
            let serviceName = row.getElementsByTagName("td")[1].textContent.toLowerCase();
            let relevance = getRelevance(serviceName, input);
            return { row, relevance };
        });
        sortedRows.sort((a, b) => b.relevance - a.relevance);
        sortedRows.forEach(entry => table.appendChild(entry.row));
    }

    function getRelevance(serviceName, searchQuery) {
        if (serviceName.startsWith(searchQuery)) {
            return 2;
        } else if (serviceName.includes(searchQuery)) {
            return 1;
        }
        return 0;
    }

    // Filter services by type (using AJAX)
    function filterServicesBy(type) {
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "${pageContext.request.contextPath}/RetrieveServiceServlet?filter=" + type, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.querySelector("#services tbody").innerHTML = xhr.responseText;
            }
        };
        xhr.send();
    }

    // User filtering functions using AJAX
    function filterUsersByName() {
        let input = document.getElementById("nameSearch").value.trim();
        if (input === "") {
            fetchAllUsers();
            return;
        }
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "${pageContext.request.contextPath}/FilterUsersByAddressServlet?searchQuery=" + encodeURIComponent(input), true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.querySelector("#members tbody").innerHTML = xhr.responseText;
            }
        };
        xhr.send();
    }

    function fetchAllUsers() {
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "${pageContext.request.contextPath}/FilterUsersByAddressServlet", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.querySelector("#members tbody").innerHTML = xhr.responseText;
            }
        };
        xhr.send();
    }

    function filterUsersByPostal() {
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "${pageContext.request.contextPath}/FilterUsersByPostalServlet?sort=postal", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.querySelector("#members tbody").innerHTML = xhr.responseText;
            }
        };
        xhr.send();
    }

    // -------------------------------
    // Initialization on Page Load
    // -------------------------------
    document.addEventListener('DOMContentLoaded', function() {
        // If a query parameter “activeTab” is passed, activate that tab; otherwise default to serviceCategories.
        const params = new URLSearchParams(window.location.search);
        const activeTab = params.get("activeTab") || "serviceCategories";
        document.querySelector(`button[onclick*='${activeTab}']`).click();
        fetchAllUsers();
    });
</script>
</body>
</html>
