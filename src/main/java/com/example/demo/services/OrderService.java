package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;

@Service
public class OrderService {
	public void handleSuccessfulPayment(Event event) {
		System.out.println("STRIPE_API_VERSION= " + Stripe.API_VERSION);

		Session session = (Session) event.getDataObjectDeserializer().getObject().get();
		String sessionId = session.getId();

		try {
			// Expand each line item
			SessionListLineItemsParams listItemsParams = SessionListLineItemsParams.builder().addExpand("data.price.product")
					.build();

			// Get the data for each of them
			List<LineItem> lineItems = session.listLineItems(listItemsParams).getData();

			// Store a dictionary of product IDs to quantities
			Map<String, Long> orderedProducts = new HashMap<>();

			for (LineItem item : lineItems) {
				String productId = item.getPrice().getProductObject().getMetadata().get("product_id");
				if (productId == null) {
					System.out.println("Product ID not found in metadata");
					continue;
				}
				long quantity = item.getQuantity();
				orderedProducts.put(productId, quantity);
			}

		} catch (Exception e) {
			System.out.println("Error handling stripe event: " + e.getMessage());
		}
	}
}
