package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Order;
import it.unicam.cs.ids.agriplatform.models.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void testFindByUserIdOrderByOrderDateDesc() {
        // Given: ordini di diversi utenti
        Long userId1 = 100L;
        Long userId2 = 200L;

        Order order1 = createOrder(userId1, OrderStatus.PENDING, LocalDateTime.now().minusDays(5), 50.0);
        Order order2 = createOrder(userId1, OrderStatus.CONFIRMED, LocalDateTime.now().minusDays(2), 100.0);
        Order order3 = createOrder(userId2, OrderStatus.DELIVERED, LocalDateTime.now().minusDays(1), 75.0);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When: cerco ordini di userId1
        List<Order> userOrders = orderRepository.findByUserIdOrderByOrderDateDesc(userId1);

        // Then: solo ordini di userId1, ordinati per data discendente
        assertThat(userOrders).hasSize(2);
        assertThat(userOrders.get(0).getTotalPrice()).isEqualTo(100.0);
        assertThat(userOrders.get(1).getTotalPrice()).isEqualTo(50.0);
    }

    @Test
    void testFindByStatusOrderByOrderDateDesc() {
        // Given: ordini con stati diversi
        Order pending1 = createOrder(100L, OrderStatus.PENDING, LocalDateTime.now().minusDays(3), 50.0);
        Order pending2 = createOrder(101L, OrderStatus.PENDING, LocalDateTime.now().minusDays(1), 75.0);
        Order confirmed = createOrder(102L, OrderStatus.CONFIRMED, LocalDateTime.now().minusDays(2), 100.0);

        orderRepository.save(pending1);
        orderRepository.save(pending2);
        orderRepository.save(confirmed);

        // When: cerco ordini PENDING
        List<Order> pendingOrders = orderRepository.findByStatusOrderByOrderDateDesc(OrderStatus.PENDING);

        // Then: solo ordini PENDING, ordinati per data discendente
        assertThat(pendingOrders).hasSize(2);
        assertThat(pendingOrders.get(0).getTotalPrice()).isEqualTo(75.0);
        assertThat(pendingOrders.get(1).getTotalPrice()).isEqualTo(50.0);
    }

    @Test
    void testCountByUserId() {
        // Given: ordini di diversi utenti
        Long userId = 100L;
        Order order1 = createOrder(userId, OrderStatus.PENDING, LocalDateTime.now(), 50.0);
        Order order2 = createOrder(userId, OrderStatus.CONFIRMED, LocalDateTime.now(), 75.0);
        Order order3 = createOrder(200L, OrderStatus.DELIVERED, LocalDateTime.now(), 100.0);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When: conto ordini dell'utente
        long count = orderRepository.countByUserId(userId);

        // Then: conta solo gli ordini dell'utente specificato
        assertThat(count).isEqualTo(2);
    }

    private Order createOrder(Long userId, OrderStatus status, LocalDateTime orderDate, double totalPrice) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(status);
        order.setOrderDate(orderDate);
        order.setTotalPrice(totalPrice);
        order.setDeliveryAddress("Test Address");
        return order;
    }
}
