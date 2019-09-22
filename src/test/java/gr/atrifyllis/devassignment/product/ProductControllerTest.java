package gr.atrifyllis.devassignment.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.atrifyllis.devassignment.support.MockMvcBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
        ProductDto sampleProductDto = ProductSampleCreator.getProductDto();
        this.mockMvc
                .perform(post("/products").accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.price", is(sampleProductDto.getPrice().doubleValue())));
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
        ProductDto zeroPriceProduct = ProductDto.builder().name("product with 0 price").price(new BigDecimal("0")).build();
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

    @Test
    public void shouldUpdateProductPrice() throws Exception {
        Long productId = this.productService.findAll().get(0).getId();
        ProductDto sampleProductDto = ProductSampleCreator.getProductDto();
        this.mockMvc
                .perform(put("/products/" + productId).accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.price", is(sampleProductDto.getPrice().doubleValue())));
    }

    @Test
    public void shouldNotUpdateProductNotFound() throws Exception {
        ProductDto zeroPriceProduct = ProductDto.builder().name("product does not exist").price(BigDecimal.ONE).build();
        String invalidProductId = "11212211212";
        this.mockMvc
                .perform(put("/products/" + invalidProductId).accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zeroPriceProduct))
                        .characterEncoding("utf-8") // needed for print()
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", containsString("Product with id " + invalidProductId)));
    }

    // TODO add invalid update product tests?
}
