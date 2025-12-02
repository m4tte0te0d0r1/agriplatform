package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.exception.HttpException;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private ProductService productService;

    private User producerUser;
    private User customerUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        producerUser = new User();
        producerUser.setId(1L);
        producerUser.setRole(Role.PRODUCER);

        customerUser = new User();
        customerUser.setId(2L);
        customerUser.setRole(Role.CUSTOMER);

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setName("Test Product");
        testProduct.setPrice(25.0);
        testProduct.setUserId(1L);
        testProduct.setApproved(false);
    }

    @Test
    void testApproveProduct_Success() {
        // Given: prodotto non approvato
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When: approvo il prodotto
        Optional<Product> result = productService.approveProduct(100L);

        // Then: prodotto viene approvato
        assertThat(result).isPresent();
        verify(productRepository).save(argThat(p -> p.isApproved()));
    }

    @Test
    void testRejectProduct_Success() {
        // Given: prodotto approvato
        testProduct.setApproved(true);
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When: rifiuto il prodotto
        Optional<Product> result = productService.rejectProduct(100L);

        // Then: prodotto non è più approvato
        assertThat(result).isPresent();
        verify(productRepository).save(argThat(p -> !p.isApproved()));
    }

    @Test
    void testDeleteProduct_Success_AsOwner() {
        // Given: utente proprietario del prodotto
        when(userContext.getCurrentUser()).thenReturn(producerUser);
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).deleteById(100L);

        // When: elimino il prodotto
        boolean result = productService.deleteProduct(100L);

        // Then: prodotto viene eliminato
        assertThat(result).isTrue();
        verify(productRepository, times(1)).deleteById(100L);
    }

    @Test
    void testDeleteProduct_Forbidden_NotOwner() {
        // Given: utente NON proprietario del prodotto
        when(userContext.getCurrentUser()).thenReturn(customerUser);
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));

        // When/Then: lancia eccezione per mancanza di permessi
        assertThatThrownBy(() -> productService.deleteProduct(100L))
                .isInstanceOf(HttpException.class)
                .hasMessageContaining("permission");

        verify(productRepository, never()).deleteById(any());
    }
}
