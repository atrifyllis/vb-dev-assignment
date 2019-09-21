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
}
