package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.output.OrderResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Order;
import it.unicam.cs.ids.agriplatform.models.OrderStatus;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.OrderRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductPackageRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPackageRepository productPackageRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(100L);
        testUser.setEmail("test@test.com");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUserId(100L);
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setTotalPrice(150.0);
        testOrder.setDeliveryAddress("Test Address");
        testOrder.setItems(new ArrayList<>());
    }

    @Test
    void testCancelOrder_Success() {
        // Given: ordine PENDING dell'utente corrente
        when(userContext.getCurrentUser()).thenReturn(testUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When: cancello l'ordine
        Optional<OrderResponseDTO> result = orderService.cancelOrder(1L);

        // Then: ordine viene cancellato
        assertThat(result).isPresent();
        verify(orderRepository).save(argThat(order -> order.getStatus() == OrderStatus.CANCELLED));
    }

    @Test
    void testCancelOrder_WrongStatus() {
        // Given: ordine già SHIPPED (non cancellabile)
        testOrder.setStatus(OrderStatus.SHIPPED);
        when(userContext.getCurrentUser()).thenReturn(testUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When/Then: lancia eccezione
        assertThatThrownBy(() -> orderService.cancelOrder(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cannot be cancelled");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCancelOrder_NotOwner() {
        // Given: ordine di un altro utente
        testOrder.setUserId(999L); // Diverso da testUser.getId()
        when(userContext.getCurrentUser()).thenReturn(testUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When/Then: lancia eccezione
        assertThatThrownBy(() -> orderService.cancelOrder(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("your own orders");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testDeleteOrder_OnlyPendingOrCancelled() {
        // Given: ordine CONFIRMED (non eliminabile)
        testOrder.setStatus(OrderStatus.CONFIRMED);
        when(userContext.getCurrentUser()).thenReturn(testUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When/Then: lancia eccezione
        assertThatThrownBy(() -> orderService.deleteOrder(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDING or CANCELLED");

        verify(orderRepository, never()).deleteById(any());
    }
}
