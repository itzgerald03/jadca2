package servlets;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/CA1/create-payment-intent")
public class CreatePaymentIntentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String STRIPE_SECRET_KEY = "sk_test_51QqVxVPTnTma0uQldMXUwyv13VtjkeDTZDzgjthfAixKroSziLuw8jVQzSaro5UGkzlzIflx5hXG2k1B8tIm4U4c00IL2vOpIN";
    private final Gson gson = new Gson();

    @Override
    public void init() {
        // Initialize Stripe with secret key
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            // Read JSON data from request
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            // Parse JSON using Gson
            JsonObject jsonData = gson.fromJson(buffer.toString(), JsonObject.class);
            long amount = jsonData.get("amount").getAsLong();
            String serviceName = jsonData.get("serviceName").getAsString();

            // Create PaymentIntent
            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("sgd")
                .setDescription(serviceName)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods
                        .builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

            PaymentIntent intent = PaymentIntent.create(createParams);

            // Create response JSON
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("clientSecret", intent.getClientSecret());
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            response.setStatus(500);
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            out.print(error.toString());
            e.printStackTrace(); // For debugging
        }
    }
}