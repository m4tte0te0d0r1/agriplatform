package it.unicam.cs.ids.agriplatform.repositories;

import it.unicam.cs.ids.agriplatform.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testSaveAndFindProduct() {
        // Given: un nuovo prodotto
        Product product = new Product();
        product.setName("Olio d'Oliva Bio");
        product.setPrice(15.50);
        product.setQuantity(100);
        product.setUserId(1L);
        product.setApproved(false);
        product.setAvailable(true);

        // When: salvo il prodotto
        Product saved = productRepository.save(product);

        // Then: posso recuperarlo
        assertThat(saved.getId()).isGreaterThan(0);
        Product found = productRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("Olio d'Oliva Bio");
        assertThat(found.getPrice()).isEqualTo(15.50);
    }

    @Test
    void testFindAll_FilterByApproved() {
        // Given: prodotti approvati e non approvati
        Product approved1 = createProduct("Product 1", true);
        Product approved2 = createProduct("Product 2", true);
        Product pending = createProduct("Product 3", false);

        productRepository.save(approved1);
        productRepository.save(approved2);
        productRepository.save(pending);

        // When: recupero tutti i prodotti
        List<Product> allProducts = productRepository.findAll();

        // Then: trovo tutti e 3
        assertThat(allProducts).hasSize(3);

        // Filter manualmente per approved
        long approvedCount = allProducts.stream().filter(Product::isApproved).count();
        assertThat(approvedCount).isEqualTo(2);
    }

    private Product createProduct(String name, boolean approved) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(10.0);
        product.setQuantity(50);
        product.setUserId(1L);
        product.setApproved(approved);
        product.setAvailable(true);
        return product;
    }
}
