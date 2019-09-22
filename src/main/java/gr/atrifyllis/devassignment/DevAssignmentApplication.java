package gr.atrifyllis.devassignment;

import gr.atrifyllis.devassignment.order.OrderRepository;
import gr.atrifyllis.devassignment.order.OrderSampleCreator;
import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class DevAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevAssignmentApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializer(ProductRepository productRepository, OrderRepository orderRepository) {
        return args -> {
            productRepository.saveAll(ProductSampleCreator.getTwoProducts());
            List<Product> persistedProducts = productRepository.findAll();
            orderRepository.save(OrderSampleCreator.getOrder(new HashSet<>(persistedProducts)));
        };
    }
}



