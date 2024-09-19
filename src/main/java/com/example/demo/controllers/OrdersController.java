package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.models.Order;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.repo.OrderRepo;
import com.example.demo.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private OrderService orderService;
    private OrderItemRepo orderItemRepo;
    private OrderRepo orderRepo;
    private List<Order> orders;

    @Autowired
    public OrdersController(OrderService orderService, OrderItemRepo orderItemRepo, OrderRepo orderRepo) {
        this.orderService = orderService;
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
        this.orders = new ArrayList<>();
    }

    @GetMapping("")
    public String viewOrders(Model model) {
        List<Order> orders = orderRepo.findAll();
        System.out.println(orders);
        model.addAttribute("orders", orders);
        return "orders/index";
    }

    // Route to view details of a specific order
    @GetMapping("/{orderId}")
    public Order viewOrderDetails(@PathVariable int orderId) {
        return orders.stream()
            .filter(order -> order.getOrderId() == orderId)
            .findFirst()
            .orElse(null); // Return null if order is not found (can be replaced by better error handling)
    }

    // Route to delete a specific order
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable int orderId) {
        boolean removed = orders.removeIf(order -> order.getOrderId() == orderId);
        if (removed) {
            return "Order " + orderId + " has been deleted.";
        } else {
            return "Order " + orderId + " not found.";
        }
    }

    // Route to update the status of a specific order
    @PutMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable int orderId, @RequestParam String status) {
        Order order = orders.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
        if (order != null) {
            if (List.of("processing", "cancelled", "shipping", "completed").contains(status)) {
                order.setStatus(status);
                return "Order " + orderId + " status updated to " + status;
            } else {
                return "Invalid status. Choose from: processing, cancelled, shipping, or completed.";
            }
        } else {
            return "Order not found.";
        }
    }
}
