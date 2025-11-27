package it.unicam.cs.ids.agriplatform.controllers.product;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductPackageDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductPackageDTO;
import it.unicam.cs.ids.agriplatform.models.ProductPackage;
import it.unicam.cs.ids.agriplatform.services.ProductPackageService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/packages")
public class ProductPackageController {

    private final ProductPackageService productPackageService;

    @Autowired
    public ProductPackageController(ProductPackageService productPackageService) {
        this.productPackageService = productPackageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPackages() {
        List<ProductPackage> packages = productPackageService.getAllPackages();
        return ApiResponse.ok("Product packages retrieved successfully", packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable Long id) {
        Optional<ProductPackage> productPackage = productPackageService.getPackageById(id);
        return productPackage
                .map(value -> ApiResponse.ok("Product package found", value))
                .orElseGet(() -> ApiResponse.notFound("Product package not found"));
    }

    @PostMapping
    public ResponseEntity<?> createPackage(@Valid @RequestBody CreateProductPackageDTO dto) {
        ProductPackage created = productPackageService.createPackage(dto);
        return ApiResponse.created("Product package created successfully", created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable Long id, @Valid @RequestBody UpdateProductPackageDTO dto) {
        Optional<ProductPackage> updated = productPackageService.updatePackage(id, dto);
        return updated
                .map(value -> ApiResponse.ok("Product package updated successfully", value))
                .orElseGet(() -> ApiResponse.notFound("Product package not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        boolean deleted = productPackageService.deletePackage(id);
        if (deleted) {
            return ApiResponse.ok("Product package deleted successfully");
        } else {
            return ApiResponse.notFound("Product package not found");
        }
    }
}
