package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.order.CreateOrderDTO;
import it.unicam.cs.ids.agriplatform.dto.input.order.OrderItemDTO;
import it.unicam.cs.ids.agriplatform.dto.input.order.UpdateOrderStatusDTO;
import it.unicam.cs.ids.agriplatform.dto.output.OrderResponseDTO;
import it.unicam.cs.ids.agriplatform.dto.output.OrderItemResponseDTO;
import it.unicam.cs.ids.agriplatform.models.*;
import it.unicam.cs.ids.agriplatform.repositories.OrderRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductPackageRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final UserContext userContext;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
            ProductPackageRepository productPackageRepository, UserContext userContext) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.userContext = userContext;
    }

    /**
     * Get all orders (admin function)
     */
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get order by ID
     */
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Get all orders for a specific user
     */
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get current user's orders
     */
    public List<OrderResponseDTO> getCurrentUserOrders() {
        User currentUser = userContext.getCurrentUser();
        return getOrdersByUserId(currentUser.getId());
    }

    /**
     * Get orders by status
     */
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get current user's orders by status
     */
    public List<OrderResponseDTO> getCurrentUserOrdersByStatus(OrderStatus status) {
        User currentUser = userContext.getCurrentUser();
        return orderRepository.findByUserIdAndStatusOrderByOrderDateDesc(currentUser.getId(), status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new order
     */
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderDTO orderDTO) {
        User currentUser = userContext.getCurrentUser();

        // Create order items and calculate total
        List<OrderItem> orderItems = orderDTO.items().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        // Create order
        Order order = new Order(
                0L,
                currentUser.getId(),
                LocalDateTime.now(),
                totalPrice,
                OrderStatus.PENDING,
                orderDTO.deliveryAddress(),
                orderDTO.notes(),
                orderItems);

        // Set bidirectional relationship
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    /**
     * Update order status
     */
    public Optional<OrderResponseDTO> updateOrderStatus(Long id, UpdateOrderStatusDTO statusDTO) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(statusDTO.status());
            Order updatedOrder = orderRepository.save(order);
            return mapToDTO(updatedOrder);
        });
    }

    /**
     * Cancel an order (only if PENDING or CONFIRMED)
     */
    public Optional<OrderResponseDTO> cancelOrder(Long id) {
        User currentUser = userContext.getCurrentUser();

        return orderRepository.findById(id).map(order -> {
            // Check if user owns the order
            if (order.getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only cancel your own orders");
            }

            // Check if order can be cancelled
            if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
                throw new IllegalStateException("Order cannot be cancelled in current status: " + order.getStatus());
            }

            order.setStatus(OrderStatus.CANCELLED);
            Order updatedOrder = orderRepository.save(order);
            return mapToDTO(updatedOrder);
        });
    }

    /**
     * Delete an order (admin function or user if PENDING)
     */
    @Transactional
    public boolean deleteOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            User currentUser = userContext.getCurrentUser();

            // Check if user owns the order
            if (order.getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only delete your own orders");
            }

            // Only allow deletion of PENDING or CANCELLED orders
            if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CANCELLED) {
                throw new IllegalStateException("Only PENDING or CANCELLED orders can be deleted");
            }

            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get order statistics for current user
     */
    public OrderStatistics getCurrentUserStatistics() {
        User currentUser = userContext.getCurrentUser();
        long totalOrders = orderRepository.countByUserId(currentUser.getId());

        List<Order> userOrders = orderRepository.findByUserIdOrderByOrderDateDesc(currentUser.getId());
        double totalSpent = userOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalPrice)
                .sum();

        long pendingOrders = userOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .count();

        long completedOrders = userOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        return new OrderStatistics(totalOrders, totalSpent, pendingOrders, completedOrders);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Create an order item from DTO
     */
    private OrderItem createOrderItem(OrderItemDTO itemDTO) {
        double unitPrice;

        // Get price based on item type
        if (itemDTO.itemType() == OrderItemType.PRODUCT) {
            Product product = productRepository.findById(itemDTO.itemId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + itemDTO.itemId()));
            unitPrice = product.getPrice();
        } else {
            ProductPackage productPackage = productPackageRepository.findById(itemDTO.itemId())
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + itemDTO.itemId()));
            unitPrice = productPackage.getPrice();
        }

        double totalPrice = unitPrice * itemDTO.quantity();

        return new OrderItem(
                0L,
                null, // Will be set when adding to order
                itemDTO.itemType(),
                itemDTO.itemId(),
                itemDTO.quantity(),
                unitPrice,
                totalPrice);
    }

    /**
     * Map Order entity to OrderResponseDTO
     */
    private OrderResponseDTO mapToDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(this::mapItemToDTO)
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getDeliveryAddress(),
                order.getNotes(),
                items);
    }

    /**
     * Map OrderItem entity to OrderItemResponseDTO
     */
    private OrderItemResponseDTO mapItemToDTO(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getId(),
                item.getItemType(),
                item.getItemId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice());
    }

    /**
     * Inner class for order statistics
     */
    public record OrderStatistics(
            long totalOrders,
            double totalSpent,
            long pendingOrders,
            long completedOrders) {
    }
}
