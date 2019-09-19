package gr.atrifyllis.devassignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @GetMapping("/products")
    List<Product> getProducts() {
        return this.productService.findAll();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
class Product {

    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
class PlacedOrder {
    @Id
    private Long id;
    private String buyer;
    private LocalDateTime placedAt;
    private BigDecimal orderPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<Product> products;

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
