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

    /**
     * GET /api/products/pending - Get pending products (not approved)
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingProducts() {
        List<Product> products = productService.getPendingProducts();
        return ApiResponse.ok("Pending products retrieved successfully", products);
    }

    /**
     * GET /api/products/approved - Get approved products
     */
    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedProducts() {
        List<Product> products = productService.getApprovedProducts();
        return ApiResponse.ok("Approved products retrieved successfully", products);
    }

    /**
     * PUT /api/products/{id}/approve - Approve a product (Curator only)
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveProduct(@PathVariable Long id) {
        return productService.approveProduct(id)
                .map(product -> ApiResponse.ok("Product approved successfully", product))
                .orElse(ApiResponse.notFound("Product not found with id: " + id));
    }

    /**
     * PUT /api/products/{id}/reject - Reject a product
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectProduct(@PathVariable Long id) {
        return productService.rejectProduct(id)
                .map(product -> ApiResponse.ok("Product rejected successfully", product))
                .orElse(ApiResponse.notFound("Product not found with id: " + id));
    }
}
