package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.ProductDetailDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductDTO;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.models.ProductDetail;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

            existingProduct.setDetails(
                    productDTO.details()
                            .stream()
                            .map(dto -> new ProductDetail(
                                    dto.id(),
                                    dto.name(),
                                    dto.price(),
                                    dto.description(),
                                    dto.approved(),
                                    dto.userId(),
                                    dto.productId()))
                            .collect(Collectors.toList()));
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
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setUserId(dto.userId());
        product.setQuantity(dto.quantity());
        // product.setDetails(dto.details());
        return product;
    }

    /**
     * Map a ProductDetailDTO to a ProductDetail entity.
     */
    // private ProductDetail mapToEntity(ProductDetailDTO dto) {
    //     ProductDetail productDetail = new ProductDetail();
    //     productDetail.se(dto.name());
    //     product.setPrice(dto.price());
    //     product.setUserId(dto.userId());
    //     product.setQuantity(dto.quantity());
    //     product.setDetails(dto.details());
    //     return product;
    // }
}
