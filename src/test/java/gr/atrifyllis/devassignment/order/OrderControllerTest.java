package gr.atrifyllis.devassignment.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductDto;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import gr.atrifyllis.devassignment.product.ProductService;
import gr.atrifyllis.devassignment.support.MockMvcBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// tests won't emulate production behavior without transactional annotation and will fail
@Transactional
public class OrderControllerTest extends MockMvcBase {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    private List<Product> persistedProducts;

    @Before
    public void setUp() {

        cleanUpDatabase();

        ProductSampleCreator.getTwoProducts().stream().map(p -> new ProductDto(p.getName(), p.getCurrentPrice())).forEach(this.productService::create);
        persistedProducts = productService.findAll();
        orderService.create(OrderSampleCreator.getOrderDtoFromOrder(OrderSampleCreator.getOrder(persistedProducts), persistedProducts));
    }


    @Test
    public void shouldReturnOrderList() throws Exception {
        this.mockMvc
                .perform(get("/orders").accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].buyer", is("valid@email.com")))
                .andExpect(jsonPath("$[0].totalPrice", is(101.54)));
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        OrderDto sampleOrderDto = OrderSampleCreator.getOrderDtoFromOrder(OrderSampleCreator.getOrder(persistedProducts), persistedProducts);
        this.mockMvc
                .perform(post("/orders").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrderDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")) // needed for print()
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lineProducts", hasSize(2)))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalPrice", is(getTotalPriceOfProducts(persistedProducts))));
    }

    @Test
    public void shouldNotCreateOrderWithoutProducts() throws Exception {
        OrderDto orderWithoutProducts = OrderDto.builder().buyer("valid@email.com").build();
        this.mockMvc
                .perform(post("/orders").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderWithoutProducts))
                        .characterEncoding("utf-8") // needed for print
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.productIds").exists());
    }

    @Test
    public void shouldNotCreateOrderWithoutBuyer() throws Exception {
        OrderDto orderWithoutBuyer = OrderDto.builder().productIds(OrderSampleCreator.getIdsFromProducts(persistedProducts)).build();
        this.mockMvc
                .perform(post("/orders").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderWithoutBuyer))
                        .characterEncoding("utf-8") // needed for print
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.buyer").exists());
    }

    @Test
    public void shouldNotCreateOrderWithWrongProductIds() throws Exception {
        OrderDto orderWithoutBuyer = OrderDto.builder().buyer("valid@email.com").productIds(new HashSet<>(Arrays.asList(persistedProducts.get(0).getId(), 111L))).build();
        this.mockMvc
                .perform(post("/orders").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderWithoutBuyer))
                        .characterEncoding("utf-8") // needed for print
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Unable to find Product with id 111")));
    }


    private double getTotalPriceOfProducts(List<Product> persistedProducts) {
        return persistedProducts.stream().map(Product::getCurrentPrice).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
    }
}

