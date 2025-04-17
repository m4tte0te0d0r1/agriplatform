package it.unicam.cs.ids.agriplatform.controllers.product;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductDTO;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.services.ProductService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
    }

    /**
     * Get all products.
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ApiResponse.ok("Products retrieved successfully", products);
    }

    /**
     * Get a product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product
                .map(value -> ApiResponse.ok("Product found", value))
                .orElseGet(() -> ApiResponse.notFound("Product not found"));
    }

    /**
     * Create a new product.
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductDTO productDTO) {
        Product created = productService.createProduct(productDTO);
        return ApiResponse.created("Product created successfully", created);
    }

    /**
     * Update an existing product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO productDTO) {
        Optional<Product> updated = productService.updateProduct(id, productDTO);
        return updated
                .map(value -> ApiResponse.ok("Product updated successfully", value))
                .orElseGet(() -> ApiResponse.notFound("Product not found"));
    }

    /**
     * Delete a product by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ApiResponse.ok("Product deleted successfully");
        } else {
            return ApiResponse.notFound("Product not found");
        }
    }
}
