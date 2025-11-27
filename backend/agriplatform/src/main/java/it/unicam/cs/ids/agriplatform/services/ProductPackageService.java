package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.product.CreateProductPackageDTO;
import it.unicam.cs.ids.agriplatform.dto.input.product.UpdateProductPackageDTO;
import it.unicam.cs.ids.agriplatform.exception.HttpException;
import it.unicam.cs.ids.agriplatform.models.Product;
import it.unicam.cs.ids.agriplatform.models.ProductPackage;
import it.unicam.cs.ids.agriplatform.models.Role;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.ProductPackageRepository;
import it.unicam.cs.ids.agriplatform.repositories.ProductRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductPackageService {

    private final ProductPackageRepository productPackageRepository;
    private final ProductRepository productRepository;
    private final UserContext userContext;

    @Autowired
    public ProductPackageService(ProductPackageRepository productPackageRepository,
            ProductRepository productRepository,
            UserContext userContext) {
        this.productPackageRepository = productPackageRepository;
        this.productRepository = productRepository;
        this.userContext = userContext;
    }

    public List<ProductPackage> getAllPackages() {
        return productPackageRepository.findAll();
    }

    public Optional<ProductPackage> getPackageById(Long id) {
        return productPackageRepository.findById(id);
    }

    public ProductPackage createPackage(CreateProductPackageDTO dto) {
        User currentUser = userContext.getCurrentUser();
        validatePackageCreationRole(currentUser);

        ProductPackage productPackage = new ProductPackage();
        productPackage.setName(dto.name());
        productPackage.setPrice(dto.price());
        productPackage.setDescription(dto.description());
        productPackage.setUserId(currentUser.getId());

        List<Product> products = productRepository.findAllById(dto.productIds());
        productPackage.setProducts(products);

        return productPackageRepository.save(productPackage);
    }

    public Optional<ProductPackage> updatePackage(Long id, UpdateProductPackageDTO dto) {
        User currentUser = userContext.getCurrentUser();

        return productPackageRepository.findById(id).map(existingPackage -> {
            validatePackageModificationRole(currentUser, existingPackage);

            existingPackage.setName(dto.name());
            existingPackage.setPrice(dto.price());
            existingPackage.setDescription(dto.description());

            if (dto.productIds() != null) {
                List<Product> products = productRepository.findAllById(dto.productIds());
                existingPackage.setProducts(products);
            }

            return productPackageRepository.save(existingPackage);
        });
    }

    public boolean deletePackage(Long id) {
        User currentUser = userContext.getCurrentUser();
        Optional<ProductPackage> packageOpt = productPackageRepository.findById(id);

        if (packageOpt.isPresent()) {
            validatePackageModificationRole(currentUser, packageOpt.get());
            productPackageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validatePackageCreationRole(User user) {
        Role role = user.getRole();
        if (role != Role.PRODUCER &&
                role != Role.PRODUCTS_TRANSFORMATOR &&
                role != Role.PRODUCTS_DISTRIBUTOR) {
            throw new HttpException("User does not have permission to create product packages", HttpStatus.FORBIDDEN);
        }
    }

    private void validatePackageModificationRole(User user, ProductPackage productPackage) {
        if (user.getId() != productPackage.getUserId() &&
                user.getRole() != Role.ADMIN &&
                user.getRole() != Role.CURATOR) {
            throw new HttpException("User does not have permission to modify this package", HttpStatus.FORBIDDEN);
        }
    }
}
