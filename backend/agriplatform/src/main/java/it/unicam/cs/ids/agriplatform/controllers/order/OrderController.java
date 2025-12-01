package it.unicam.cs.ids.agriplatform.controllers.order;

import it.unicam.cs.ids.agriplatform.dto.input.order.CreateOrderDTO;
import it.unicam.cs.ids.agriplatform.dto.input.order.UpdateOrderStatusDTO;
import it.unicam.cs.ids.agriplatform.dto.output.OrderResponseDTO;
import it.unicam.cs.ids.agriplatform.models.OrderStatus;
import it.unicam.cs.ids.agriplatform.services.OrderService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * GET /api/orders - Get all orders (admin)
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ApiResponse.ok("Orders retrieved successfully", orders);
    }

    /**
     * GET /api/orders/{id} - Get a specific order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ApiResponse.ok("Order retrieved successfully", order))
                .orElse(ApiResponse.notFound("Order not found with id: " + id));
    }

    /**
     * GET /api/orders/user/{userId} - Get all orders for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ApiResponse.ok("User orders retrieved successfully", orders);
    }

    /**
     * GET /api/orders/my - Get current user's orders
     */
    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUserOrders() {
        List<OrderResponseDTO> orders = orderService.getCurrentUserOrders();
        return ApiResponse.ok("Your orders retrieved successfully", orders);
    }

    /**
     * GET /api/orders/status/{status} - Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ApiResponse.ok("Orders with status " + status + " retrieved successfully", orders);
    }

    /**
     * GET /api/orders/my/status/{status} - Get current user's orders by status
     */
    @GetMapping("/my/status/{status}")
    public ResponseEntity<?> getCurrentUserOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getCurrentUserOrdersByStatus(status);
        return ApiResponse.ok("Your orders with status " + status + " retrieved successfully", orders);
    }

    /**
     * POST /api/orders - Create a new order
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderDTO orderDTO) {
        try {
            OrderResponseDTO createdOrder = orderService.createOrder(orderDTO);
            return ApiResponse.created("Order created successfully", createdOrder);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * PUT /api/orders/{id}/status - Update order status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDTO statusDTO) {
        return orderService.updateOrderStatus(id, statusDTO)
                .map(order -> ApiResponse.ok("Order status updated successfully", order))
                .orElse(ApiResponse.notFound("Order not found with id: " + id));
    }

    /**
     * PUT /api/orders/{id}/cancel - Cancel an order
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            return orderService.cancelOrder(id)
                    .map(order -> ApiResponse.ok("Order cancelled successfully", order))
                    .orElse(ApiResponse.notFound("Order not found with id: " + id));
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * DELETE /api/orders/{id} - Delete an order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            boolean deleted = orderService.deleteOrder(id);
            if (deleted) {
                return ApiResponse.ok("Order deleted successfully", (Void) null);
            }
            return ApiResponse.notFound("Order not found with id: " + id);
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * GET /api/orders/my/statistics - Get current user's order statistics
     */
    @GetMapping("/my/statistics")
    public ResponseEntity<?> getCurrentUserStatistics() {
        OrderService.OrderStatistics stats = orderService.getCurrentUserStatistics();
        return ApiResponse.ok("Statistics retrieved successfully", stats);
    }
}
