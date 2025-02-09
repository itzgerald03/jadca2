<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*, dbaccess.CartItem, java.util.List, java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Services</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/serviceBooking.css">
    <script src="https://js.stripe.com/v3/"></script>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <% 
        if (session.getAttribute("userRole") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get cart items
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        // Calculate total price
        double totalPrice = 0;
        for (CartItem item : cart) {
            totalPrice += item.getTotal();
        }
    %>

    <section class="booking-section">
        <div class="container">
            <h1>Book Your Services</h1>
           	<form id="payment-form" action="${pageContext.request.contextPath}/CA1/ProcessBookingServlet" method="post">
                <div class="booking-content">
                    <!-- Left Section -->
                    <div class="left-panel">
                        <!-- Services Summary -->
                        <h2>Selected Services</h2>
                        <div class="services-summary">
                            <% for (CartItem item : cart) { %>
                                <div class="service-item">
                                    <h3><%= item.getServiceName() %></h3>
                                    <p class="description"><%= item.getDescription() %></p>
                                    <p class="quantity">Quantity: <%= item.getQuantity() %></p>
                                    <p class="price">Price: S$ <%= String.format("%.2f", item.getTotal()) %></p>
                                    <input type="hidden" name="serviceIds" value="<%= item.getServiceId() %>">
                                    <input type="hidden" name="quantities" value="<%= item.getQuantity() %>">
                                </div>
                            <% } %>
                        </div>

                        <h2>Preferred Date and Time</h2>
                        <input type="datetime-local" name="appointmentDate" class="datetime-picker" required>

                        <h2>About Your Property</h2>
                        <select name="propertyType" class="property-type" required>
                            <option value="">Select Property Type</option>
                            <option value="Apartment">Apartment</option>
                            <option value="House">House</option>
                            <option value="Office">Office</option>
                        </select>

                        <h2>Your Details</h2>
                        <input type="text" name="name" placeholder="Name" class="user-input" value="<%= session.getAttribute("username") != null ? session.getAttribute("username") : "" %>" required>
                        <input type="text" name="postalCode" placeholder="Postal Code" class="user-input" required>
                        <textarea name="address" placeholder="Address" class="user-input" rows="3" required></textarea>
                        <input type="email" name="email" placeholder="Email Address" class="user-input" value="<%= session.getAttribute("email") != null ? session.getAttribute("email") : "" %>" required>

                        <h2>Special Request</h2>
                        <textarea name="specialRequest" placeholder="Enter any additional details or requests here..." class="user-input" rows="3"></textarea>
                    </div>

                    <!-- Right Section -->
                    <div class="right-panel">
                        <h2>Payment Details</h2>
                        <div class="summary-card">
                            <div class="payment-summary">
                                <h3>Services Summary</h3>
                                <% for (CartItem item : cart) { %>
                                    <div class="summary-item">
                                        <span class="service-name"><%= item.getServiceName() %></span>
                                        <span class="service-quantity">x<%= item.getQuantity() %></span>
                                        <span class="service-price">S$ <%= String.format("%.2f", item.getTotal()) %></span>
                                    </div>
                                <% } %>
                                <div class="total-price">
                                    <strong>Total Amount:</strong> 
                                    <span>S$ <%= String.format("%.2f", totalPrice) %></span>
                                </div>
                            </div>

                            <div class="payment-section">
                                <!-- Card Number Field -->
                                <div id="card-number-element"></div>

                                <!-- Expiry Date Field -->
                                <div id="card-expiry-element"></div>

                                <!-- CVC Field -->
                                <div id="card-cvc-element"></div>

                                <!-- Error display -->
                                <div id="card-errors" role="alert"></div>
                            </div>

                            <button id="submit-button" type="submit" class="confirm-button">
                                <div class="spinner hidden" id="spinner"></div>
                                <span id="button-text">Pay and Confirm Booking</span>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>

    <%@ include file="footer.jsp" %>

    <script>
        const stripe = Stripe('pk_test_51QqVxVPTnTma0uQlwRIy9TA2D0RkDmVuZ8BOnp7cBYGNcW2c2dZFPMS7c0hQcjpKHiFkiatmDF3Z4O8l4fMhNSdX00ibGzh7Pg');
        const elements = stripe.elements();

        const style = {
            base: {
                fontSize: '16px',
                color: '#32325d',
                fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
                '::placeholder': {
                    color: '#aab7c4'
                },
                padding: '12px'
            },
            invalid: {
                color: '#fa755a',
                iconColor: '#fa755a'
            }
        };

        // Create separate Stripe elements for Card number, Expiry, and CVC (no ZIP)
        const cardNumber = elements.create('cardNumber', {style: style});
        const cardExpiry = elements.create('cardExpiry', {style: style});
        const cardCvc = elements.create('cardCvc', {style: style});

        // Mount the elements to the DOM
        cardNumber.mount('#card-number-element');
        cardExpiry.mount('#card-expiry-element');
        cardCvc.mount('#card-cvc-element');

        cardNumber.addEventListener('change', function(event) {
            const displayError = document.getElementById('card-errors');
            if (event.error) {
                displayError.textContent = event.error.message;
            } else {
                displayError.textContent = '';
            }
        });

        // Form submission
        const form = document.getElementById('payment-form');
        const submitButton = document.getElementById('submit-button');
        const spinner = document.getElementById('spinner');
        const buttonText = document.getElementById('button-text');

        form.addEventListener('submit', async function(event) {
            event.preventDefault();

            submitButton.disabled = true;
            spinner.classList.remove('hidden');
            buttonText.textContent = 'Processing...';

            try {
                const response = await fetch('create-payment-intent', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        amount: <%= totalPrice * 100 %>,
                        serviceName: 'Multiple Services Booking'
                    })
                });

                const data = await response.json();

                if (data.error) {
                    throw new Error(data.error);
                }

                const result = await stripe.confirmCardPayment(data.clientSecret, {
                    payment_method: {
                        card: cardNumber,
                        billing_details: {
                            name: document.querySelector('input[name="name"]').value,
                            email: document.querySelector('input[name="email"]').value
                        }
                    }
                });

                if (result.error) {
                    throw new Error(result.error.message);
                }

                if (result.paymentIntent.status === 'succeeded') {
                    const paymentIdInput = document.createElement('input');
                    paymentIdInput.setAttribute('type', 'hidden');
                    paymentIdInput.setAttribute('name', 'paymentIntentId');
                    paymentIdInput.setAttribute('value', result.paymentIntent.id);
                    form.appendChild(paymentIdInput);
                    form.submit();
                }
            } catch (error) {
                const errorElement = document.getElementById('card-errors');
                errorElement.textContent = error.message;
                submitButton.disabled = false;
                spinner.classList.add('hidden');
                buttonText.textContent = 'Pay and Confirm Booking';
            }
        });

        // Set minimum date for appointment
        const dateInput = document.querySelector('input[name="appointmentDate"]');
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        tomorrow.setHours(9, 0, 0, 0);
        dateInput.min = tomorrow.toISOString().slice(0, 16);

        const nextMonth = new Date();
        nextMonth.setMonth(nextMonth.getMonth() + 1);
        dateInput.max = nextMonth.toISOString().slice(0, 16);
    </script>
</body>
</html>
