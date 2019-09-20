package gr.atrifyllis.devassignment.product;

import gr.atrifyllis.devassignment.support.MockMvcBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductControllerTest extends MockMvcBase {

    @Autowired
    private ProductService productService;

    @Before
    public void setUp() {
        ProductSampler.getTwoProducts().forEach(this.productService::create);
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
