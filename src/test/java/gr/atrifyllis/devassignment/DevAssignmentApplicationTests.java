package gr.atrifyllis.devassignment;

import gr.atrifyllis.devassignment.support.MockMvcBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DevAssignmentApplicationTests extends MockMvcBase {

    @Autowired
    private ProductService productService;

    @Before
    public void setUp() {
        Arrays.asList(
                Product.builder()
                        .name("product test name 1")
                        .price(BigDecimal.ONE)
                        .build(),
                Product.builder()
                        .name("product test name 2")
                        .price(new BigDecimal(100.54))
                        .build()
        )
                .forEach(this.productService::create);
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("product test name 1")))
                .andExpect(jsonPath("$[1].price", is(100.54))); // weird jsonPath conversion to Double!
    }
}
