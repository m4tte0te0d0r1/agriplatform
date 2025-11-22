package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.ProductDetailDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductDTO;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.models.ProductDetail;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserContext userContext;

    @Autowired
    public ProductService(ProductRepository productRepository, UserContext userContext) {
        this.productRepository = productRepository;
        this.userContext = userContext;
    }

    /**
     * Get all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get a product by its ID.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Create a new product from a DTO.
     */
    public Product createProduct(CreateProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     */
    public Optional<Product> updateProduct(Long id, UpdateProductDTO productDTO) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDTO.name());
            existingProduct.setPrice(productDTO.price());
            existingProduct.setQuantity(productDTO.quantity());
            List<ProductDetail> details = productDTO.details()
                    .stream()
                    .map(this::mapToEntity)
                    .collect(Collectors.toList());
            existingProduct.setDetails(details);
            return productRepository.save(existingProduct);
        });
    }

    /**
     * Delete a product by ID.
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Map a CreateProductDTO to a Product entity.
     */
    private Product mapToEntity(CreateProductDTO dto) {
        User currentUser = userContext.getCurrentUser();

        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setUserId(currentUser.getId());
        product.setQuantity(dto.quantity());
        product.setAvailable(true);

        List<ProductDetail> details = dto.details()
                .stream()
                .map(detailDto -> mapToEntity(detailDto, currentUser))
                .collect(Collectors.toList());

        // Set bidirectional relationship
        details.forEach(detail -> detail.setProduct(product));
        product.setDetails(details);

        return product;
    }

    /**
     * Map a ProductDetailDTO to a ProductDetail entity.
     */
    private ProductDetail mapToEntity(ProductDetailDTO dto, User currentUser) {
        ProductDetail productDetail = new ProductDetail();
        // productDetail.setId(dto.id()); // ID is usually generated, but if DTO has it
        // and we want to force it... usually for create we don't set ID.
        // Assuming create, we might ignore ID or if it's an update logic mixed in.
        // But looking at the DTO, it has ID. If it's for creation, ID should probably
        // be ignored or null.
        // However, keeping previous logic of setting ID if present, though risky for
        // IDENTITY generation.
        // if (dto.id() != null) {
        // productDetail.setId(dto.id());
        // }

        productDetail.setName(dto.name());
        productDetail.setPrice(dto.price());
        productDetail.setDescription(dto.description());
        // productDetail.setApproved(dto.approved()); // Assuming approval logic might
        // be separate or default to false/true
        productDetail.setApproved(true); // Defaulting to true for now based on previous logic or DTO

        // Use the current user as the owner of the detail as well
        productDetail.setUser(currentUser);

        return productDetail;
    }

    // Overload for update or other cases if needed, but for now we refactored the
    // main one.
    // Keeping the old one private if needed by update, but update usually maps
    // differently.
    // The update method uses mapToEntity in the stream, so we need to update that
    // call too.

    private ProductDetail mapToEntity(ProductDetailDTO dto) {
        return mapToEntity(dto, userContext.getCurrentUser());
    }
}
