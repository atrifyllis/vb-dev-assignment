package gr.atrifyllis.devassignment;

import gr.atrifyllis.devassignment.order.OrderRepository;
import gr.atrifyllis.devassignment.order.OrderSampleCreator;
import gr.atrifyllis.devassignment.product.ProductRepository;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class DevAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevAssignmentApplication.class, args);
    }

}

@Profile("!docker")
@Component
// do not initialize products when running in docker
class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    DataInitializer(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional

    // this won't run correctly if it not annotated as transactional.
    public void run(String... args) {
        productRepository.saveAll(ProductSampleCreator.getTwoProducts());
        orderRepository.save(OrderSampleCreator.getOrder(productRepository.findAll()));
    }
}



