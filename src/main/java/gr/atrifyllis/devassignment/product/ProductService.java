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

    Product create(Product p) {
        return this.productRepository.save(p);
    }
}
