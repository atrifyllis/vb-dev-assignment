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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static gr.atrifyllis.devassignment.order.OrderSampleCreator.getOrder;
import static org.assertj.core.api.Assertions.assertThat;


public class OrderRepositoryTest extends JpaTestBase {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    private List<Product> persistedProducts = new ArrayList<>();

    @Before
    public void setUp() {
        cleanUpDatabase();
        productRepository.saveAll(ProductSampleCreator.getTwoProducts());
        persistedProducts = productRepository.findAll();

    }

    @Test
    public void shouldSaveOrder() {
        orderRepository.save(getOrder(persistedProducts));

        List<PlacedOrder> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1);
    }

    @Test
    public void shouldDeleteOrderAndIntermediateEntryButNotProducts() {
        PlacedOrder persistedOrder = orderRepository.saveAndFlush(getOrder(persistedProducts)); // flush to make sure the order is saved

        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_line")).isEqualTo(2);

        orderRepository.deleteById(persistedOrder.getId());

        List<PlacedOrder> orders = orderRepository.findAll();

        assertThat(orders).hasSize(0);
        // check that the records are delete from the intermediate table
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_line")).isEqualTo(0);
        // check that products are still intact
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "product")).isEqualTo(2);

    }

    @Test
    public void shouldNotAffectOrderPriceWhenUpdatingProductPrice() {
        PlacedOrder persistedOrder = orderRepository.saveAndFlush(getOrder(persistedProducts)); // flush to make sure the order is saved
        BigDecimal oldOrderPrice = PlacedOrderResponseDto.calculateTotalOrderPrice(persistedOrder.getProducts());
        // update product price
        Product firstOrderProduct = persistedProducts.stream().findFirst().orElseThrow(IllegalStateException::new);
        firstOrderProduct.setCurrentPrice(firstOrderProduct.getCurrentPrice().add(BigDecimal.ONE));
        productRepository.saveAndFlush(firstOrderProduct);

        PlacedOrder updatedOrder = orderRepository.findById(persistedOrder.getId()).orElseThrow(IllegalStateException::new);
        // check that order price has not changed
        assertThat(PlacedOrderResponseDto.calculateTotalOrderPrice(updatedOrder.getProducts())).isEqualTo(oldOrderPrice);
    }


    @PostConstruct
    private void dataSourceInfo() throws SQLException {
        DataSource ds = context.getBean(DataSource.class);
        System.out.println(ds.getConnection().toString());
    }
}
