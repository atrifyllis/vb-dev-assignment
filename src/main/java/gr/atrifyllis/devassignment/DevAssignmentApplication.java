package gr.atrifyllis.devassignment;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DevAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevAssignmentApplication.class, args);
    }

}

@RestController
class ProductController {
    private ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieve all products.
     *
     * @return the list of Product details.
     */
    @GetMapping("/products")
    List<Product> getProducts() {
        Product test = new Product(1L, "test", BigDecimal.ZERO);
        return this.productService.findAll();
    }
}

/**
 * The product details.
 */
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
class Product {

    /**
     * The database id of the product.
     */
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    private BigDecimal price;
}

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
class PlacedOrder {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String buyer;

    private LocalDateTime placedAt;

    @javax.persistence.Column(updatable = false) // TODO check if this extra precaution is needed
    @Setter(AccessLevel.PRIVATE)
    private BigDecimal orderPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<Product> products;

    /**
     * This method should be called only on save and never again.
     */
    private void calculateTotal() {
        this.orderPrice = this.products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


interface ProductRepository extends JpaRepository<Product, Long> {

}

interface OrderRepository extends JpaRepository<PlacedOrder, Long> {

}

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

@Service
class OrderService {
    private OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    List<PlacedOrder> findAll() {
        return this.orderRepository.findAll();
    }
}
