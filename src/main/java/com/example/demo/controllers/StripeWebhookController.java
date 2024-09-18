package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.OrderService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

// A rest controller by default will return a JSON response
@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebhookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final OrderService orderService;

    @Autowired
    public StripeWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Stripe-Signature: Whenever a webhook is sent to your server, Stripe will
    // include a signature in the Stripe-Signature header for security purposes
    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
                // Extract the event from the request
                Event event = null;
                System.out.println("Stripe Webhook Called");
                try {
                    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
                } catch (Exception e) {
                    System.out.println("Error handling stripe event: " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
                }
                System.out.println(event);

                if (event.getType().equals("checkout.session.completed")) {
                    // handle processing of order
                    orderService.handleSuccessfulPayment(event);
                    System.out.println("Checkout session completed");
                }
                return ResponseEntity.ok().build();
    }
}
