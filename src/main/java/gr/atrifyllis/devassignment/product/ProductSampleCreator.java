package gr.atrifyllis.devassignment.product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ProductSampleCreator {

    public static List<Product> getTwoProducts() {
        return Arrays.asList(Product.builder()
                        .name("product test name 1")
                        .price(BigDecimal.ONE)
                        .build(),
                Product.builder()
                        .name("product test name 2")
                        .price(new BigDecimal(100.54))
                        .build());
    }

    static ProductDto getProductDto() {
        return new ProductDto("product name 1", new BigDecimal("10.50"));
    }
}
