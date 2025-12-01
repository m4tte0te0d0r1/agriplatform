package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Order;
import it.unicam.cs.ids.agriplatform.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders by user ID ordered by date descending
     */
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    /**
     * Find all orders by status
     */
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);

    /**
     * Find all orders by user and status
     */
    List<Order> findByUserIdAndStatusOrderByOrderDateDesc(Long userId, OrderStatus status);

    /**
     * Find orders between dates
     */
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find orders by user between dates
     */
    List<Order> findByUserIdAndOrderDateBetweenOrderByOrderDateDesc(Long userId, LocalDateTime startDate,
            LocalDateTime endDate);

    /**
     * Count orders by user
     */
    long countByUserId(Long userId);

    /**
     * Count orders by status
     */
    long countByStatus(OrderStatus status);
}
