<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Spotless Solutions</title>
    <link rel="stylesheet" href="css/home.css">
</head>
<body>

    <!-- Include Header -->
    <%@ include file="header.jsp" %>

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-container">
            <div class="hero-text">
                <h1>Cleaning Made Simple – Your Sparkling Spaces Await!</h1>
                <p>Discover the ultimate cleaning solutions for your home and office. Experience a cleaner, brighter future with us!</p>
                <a href="serviceCategory.jsp" class="cta-button">Book a Service</a>
            </div>
            <div class="hero-image">
                <img src="images/hero_section_picture.png" alt="Cleaning Service Illustration">
            </div>
        </div>
        <div class="hero-stats">
            <div class="stat">
                <h3>17,000+</h3>
                <p>Customers Served</p>
            </div>
            <div class="stat">
                <h3>123,456+</h3>
                <p>Sessions Completed</p>
            </div>
            <div class="stat">
                <h3>4.8*</h3>
                <p>1,000+ Google Reviews</p>
            </div>
        </div>
    </section>

    <!-- Top 3 Services -->
    <section class="services-highlights">
        <h2>Our Top 3 Most Popular Services</h2>
        <div class="service-cards">
            <div class="service-card">
                <img src="images/home_cleaning.png" alt="Home Cleaning">
                <h3>Home Cleaning</h3>
                <p>Enjoy a spotless home with our expert cleaners.</p>
            </div>
            <div class="service-card">
                <img src="images/office_cleaning.png" alt="Office Cleaning">
                <h3>Office Cleaning</h3>
                <p>Keep your workplace clean and organized.</p>
            </div>
            <div class="service-card">
                <img src="images/carpet_cleaning.png" alt="Carpet Cleaning">
                <h3>Carpet Cleaning</h3>
                <p>Remove dirt and stains for a fresh look.</p>
            </div>
        </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials">
        <h2>What Our Clients Say</h2>
        <div class="testimonial-cards">
            <div class="testimonial-card">
                <p>"Absolutely amazing service! My house feels brand new!"</p>
                <span>- Emily, Singapore</span>
                <div class="rating">Rating: ★★★★★</div>
            </div>
            <div class="testimonial-card">
                <p>"Efficient and professional cleaners. Highly recommend!"</p>
                <span>- Daniel, Singapore</span>
                <div class="rating">Rating: ★★★★☆</div>
            </div>
            <div class="testimonial-card">
                <p>"Best cleaning service I've ever used. Will book again soon!"</p>
                <span>- Sarah, Singapore</span>
                <div class="rating">Rating: ★★★★★</div>
            </div>
        </div>
    </section>
    
  <!-- How It Works Section -->
	<section class="how-it-works">
	    <h2>How It Works</h2>
	    <div class="step">
	        <div class="step-text">
	            <h3>Step 1: Select Your Desired Service</h3>
	            <p>Explore our extensive range of cleaning services and pick the one that perfectly matches your needs. Whether it’s a home, office, or specialty cleaning task, we've got you covered!</p>
	        </div>
	        <div class="step-image">
	            <img src="images/step1.png" alt="Select Service">
	        </div>
	    </div>
	    <div class="step">
	        <div class="step-text">
	            <h3>Step 2: Book a Convenient Slot</h3>
	            <p>Choose a date and time that fits your schedule. Our easy booking system ensures flexibility, allowing you to plan your cleaning without disrupting your day-to-day activities.</p>
	        </div>
	        <div class="step-image">
	            <img src="images/step2.png" alt="Book Appointment">
	        </div>
	    </div>
	    <div class="step">
	        <div class="step-text">
	            <h3>Step 3: Relax While We Do the Rest</h3>
	            <p>On the scheduled day, our professional team will arrive fully equipped and on time to deliver top-notch cleaning services. You can sit back, unwind, and enjoy the transformation of your space.</p>
	        </div>
	        <div class="step-image">
	            <img src="images/step3.png" alt="Relax While We Work">
	        </div>
	    </div>
	</section>



    <!-- FAQ Section -->
    <section class="faq-section">
        <h2>Frequently Asked Questions</h2>
        <div class="faq">
            <button class="faq-question" onclick="toggleFAQ(this)">What is Our Cleaning Service? <span class="faq-icon">+</span></button>
            <div class="faq-answer">
                <p>We provide professional cleaning services tailored to your needs, including home, office, and carpet cleaning.</p>
            </div>
        </div>
        <div class="faq">
            <button class="faq-question" onclick="toggleFAQ(this)">How do I book a service? <span class="faq-icon">+</span></button>
            <div class="faq-answer">
                <p>Visit our services page, select a category, and follow the easy booking process.</p>
            </div>
        </div>
        <div class="faq">
            <button class="faq-question" onclick="toggleFAQ(this)">How much do the services cost? <span class="faq-icon">+</span></button>
            <div class="faq-answer">
                <p>The cost depends on the type of service you choose. Check out our services page for detailed pricing.</p>
            </div>
        </div>
    </section>

    <!-- Include Footer -->
    <%@ include file="footer.jsp" %>

    <!-- FAQ Toggle Script -->
    <script>
        function toggleFAQ(button) {
            const answer = button.nextElementSibling;
            const icon = button.querySelector(".faq-icon");

            if (answer.style.display === "block") {
                answer.style.display = "none";
                icon.textContent = "+";
            } else {
                answer.style.display = "block";
                icon.textContent = "-";
            }
        }
    </script>
</body>
</html>
