package gr.atrifyllis.devassignment.product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProductSampler {

   public static Set<Product> getTwoProducts() {
       return new HashSet<>(Arrays.asList(Product.builder()
                       .name("product test name 1")
                       .price(BigDecimal.ONE)
                       .build(),
               Product.builder()
                       .name("product test name 2")
                       .price(new BigDecimal(100.54))
                       .build()));
    }
}
