package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldSaveOrder() {
        productRepository.saveAll(new HashSet<>(Arrays.asList(Product.builder()
                        .name("product test name 1")
                        .price(BigDecimal.ONE)
                        .build(),
                Product.builder()
                        .name("product test name 2")
                        .price(new BigDecimal(100.54))
                        .build())));
        Set<Product> persistedProducts = new HashSet<>(productRepository.findAll());
        PlacedOrder order = PlacedOrder.builder()
                .products(persistedProducts)
                .build();
        orderRepository.save(order);

        List<PlacedOrder> orders = orderRepository.findAll();

        Assertions.assertThat(orders).hasSize(1);
    }
}
