package com.example.demo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.models.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {
	@Value("stripe.api.secretkey")
	private String stripeSecretKey;

	@Value("stripe.api.publicKey")
	private String stripePublicKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSecretKey;
	}

	public Session createCheckOutSession(List<CartItem> cartItems, String successUrl, String cancelUrl)
			throws StripeException {
		// Create line item and pass all of it along with the payment requirements to
		// Stripe
		// Receive the checkout id from Stripe

		List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
					.setName(cartItem.getProduct().getName())
					.putMetadata("product_id", cartItem.getProduct().getId().toString())
					.build();

			var priceData = SessionCreateParams.LineItem.PriceData.builder()
					.setCurrency("usd")
					.setUnitAmount(cartItem.getProduct().getPrice().multiply(new BigDecimal(100)).longValue())
					.setProductData(productData)
					.build();

			var lineItem = SessionCreateParams.LineItem.builder()
					.setPriceData(priceData)
					.setQuantity((long) cartItem.getQuantity())
					.build();

			lineItems.add(lineItem);
		}

		SessionCreateParams params = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setCancelUrl(cancelUrl)
				.setSuccessUrl(successUrl)
				.addAllLineItem(lineItems)
				.build();

		return Session.create(params);
	}
}
