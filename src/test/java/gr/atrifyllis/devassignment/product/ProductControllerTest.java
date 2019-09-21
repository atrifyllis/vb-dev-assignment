package gr.atrifyllis.devassignment.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.atrifyllis.devassignment.support.MockMvcBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductControllerTest extends MockMvcBase {

    @Autowired
    private ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        cleanUpDatabase();

        ProductSampleCreator.getTwoProducts().stream().map(p -> new ProductDto(p.getName(), p.getPrice())).forEach(this.productService::create);
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        this.mockMvc
                .perform(get("/products").accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("product test name 1")))
                .andExpect(jsonPath("$[1].price", is(100.54))); // weird jsonPath conversion to Double!
    }


    @Test
    public void shouldCreateProduct() throws Exception {
        this.mockMvc
                .perform(post("/products").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductSampleCreator.getProductDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.price", is(10.50)));
    }

    @Test
    public void shouldNotCreateProductWithoutName() throws Exception {
        ProductDto noNameProduct = ProductDto.builder().price(new BigDecimal("10")).build();
        this.mockMvc
                .perform(post("/products").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noNameProduct))
                        .characterEncoding("utf-8") // needed for print()
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    public void shouldNotCreateProductWithZeroPrice() throws Exception {
        ProductDto zeroPriceProduct = ProductDto.builder().name("product with ) price").price(new BigDecimal("0")).build();
        this.mockMvc
                .perform(post("/products").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zeroPriceProduct))
                        .characterEncoding("utf-8") // needed for print()
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.price").exists());
    }
}
