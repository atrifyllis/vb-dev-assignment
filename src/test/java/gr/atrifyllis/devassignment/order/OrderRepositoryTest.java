package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import gr.atrifyllis.devassignment.support.JpaTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class OrderRepositoryTest extends JpaTestBase {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    private HashSet<Product> persistedProducts = new HashSet<>();


    @Before
    public void setUp() {
        productRepository.saveAll(ProductSampleCreator.getTwoProducts());
        persistedProducts = new HashSet<>(productRepository.findAll());

    }

    @Test
    public void shouldSaveOrder() {
        PlacedOrder order = PlacedOrder.builder()
                .products(persistedProducts)
                .build();
        orderRepository.save(order);

        List<PlacedOrder> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1);
    }

    @Test
    public void shouldDeleteOrderAndIntermediateEntryButNotProducts() {
        PlacedOrder order = PlacedOrder.builder()
                .products(persistedProducts)
                .build();
        PlacedOrder persistedOrder = orderRepository.saveAndFlush(order); // flush to make sure the order is saved

        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_product")).isEqualTo(2);

        orderRepository.deleteById(persistedOrder.getId());
        List<PlacedOrder> orders = orderRepository.findAll();
        // check that the records are delete from the intermediate table
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_product")).isEqualTo(0);
        // check that products are still intact
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "product")).isEqualTo(2);
        assertThat(orders).hasSize(0);

    }

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void dataSourceInfo() throws SQLException {
        DataSource ds = context.getBean(DataSource.class);
        System.out.println(ds.getConnection().toString());
    }
}
