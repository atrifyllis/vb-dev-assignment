package gr.atrifyllis.devassignment.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProductService {
    private ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<Product> findAll() {
        return this.productRepository.findAll();
    }

    Product create(ProductDto p) {
        return this.productRepository.save(Product.builder().name(p.getName()).price(p.getPrice()).build());
    }

    /**
     * Update product details if found.
     * Otherwise, throws {@link javax.persistence.EntityNotFoundException}
     * (handled by {@link gr.atrifyllis.devassignment.error.CustomGlobalExceptionHandler}.
     *
     * @param id the id of the product to update.
     * @param p  the product details to update
     * @return the updated product.
     */
    Product update(Long id, ProductDto p) {
        Product persistedProduct = this.productRepository.getOne(id);
        persistedProduct.setName(p.getName());
        persistedProduct.setPrice(p.getPrice());
        return this.productRepository.save(persistedProduct);
    }
}
