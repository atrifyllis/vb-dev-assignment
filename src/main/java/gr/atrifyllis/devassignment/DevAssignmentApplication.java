package gr.atrifyllis.devassignment;

import gr.atrifyllis.devassignment.order.OrderRepository;
import gr.atrifyllis.devassignment.order.OrderSampleCreator;
import gr.atrifyllis.devassignment.order.PlacedOrder;
import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class DevAssignmentApplication implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DevAssignmentApplication(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DevAssignmentApplication.class, args);
    }

    @Override
    @Transactional
    // this won't run correctly if it not annotated as transactional.
    public void run(String... args) {
        productRepository.saveAll(ProductSampleCreator.getTwoProducts());
        List<Product> persistedProducts = productRepository.findAll();
        PlacedOrder order = OrderSampleCreator.getOrder(persistedProducts);
        orderRepository.save(order);
    }
}



