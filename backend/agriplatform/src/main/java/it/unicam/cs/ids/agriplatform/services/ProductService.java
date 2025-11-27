package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.ProductDetailDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductDTO;
import it.unicam.cs.ids.agriplatform.exception.HttpException;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.models.ProductDetail;
import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        User currentUser = userContext.getCurrentUser();
        validateProductCreationRole(currentUser);

        Product product = mapToEntity(productDTO, currentUser);
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     */
    public Optional<Product> updateProduct(Long id, UpdateProductDTO productDTO) {
        User currentUser = userContext.getCurrentUser();

        return productRepository.findById(id).map(existingProduct -> {
            validateProductModificationRole(currentUser, existingProduct);

            existingProduct.setName(productDTO.name());
            existingProduct.setPrice(productDTO.price());
            existingProduct.setQuantity(productDTO.quantity());
            List<ProductDetail> details = productDTO.details()
                    .stream()
                    .map(detailDto -> mapToEntity(detailDto, currentUser))
                    .collect(Collectors.toList());
            existingProduct.setDetails(details);
            return productRepository.save(existingProduct);
        });
    }

    /**
     * Delete a product by ID.
     */
    public boolean deleteProduct(Long id) {
        User currentUser = userContext.getCurrentUser();
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            validateProductModificationRole(currentUser, productOpt.get());
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateProductCreationRole(User user) {
        Role role = user.getRole();
        if (role != Role.PRODUCER &&
                role != Role.PRODUCTS_TRANSFORMATOR &&
                role != Role.PRODUCTS_DISTRIBUTOR) {
            throw new HttpException("User does not have permission to create products", HttpStatus.FORBIDDEN);
        }
    }

    private void validateProductModificationRole(User user, Product product) {
        if (user.getId() != product.getUserId() &&
                user.getRole() != Role.ADMIN &&
                user.getRole() != Role.CURATOR) {
            throw new HttpException("User does not have permission to modify this product", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Map a CreateProductDTO to a Product entity.
     */
    private Product mapToEntity(CreateProductDTO dto, User currentUser) {
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
        productDetail.setName(dto.name());
        productDetail.setPrice(dto.price());
        productDetail.setDescription(dto.description());
        productDetail.setApproved(true);
        productDetail.setUser(currentUser);

        return productDetail;
    }
}
