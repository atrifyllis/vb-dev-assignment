package gr.atrifyllis.devassignment;

import gr.atrifyllis.devassignment.product.ProductRepository;
import gr.atrifyllis.devassignment.product.ProductSampler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DevAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevAssignmentApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializer(ProductRepository productRepository) {
        return args -> productRepository.saveAll(ProductSampler.getTwoProducts());
    }
}



