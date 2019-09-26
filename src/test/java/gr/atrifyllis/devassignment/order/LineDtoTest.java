package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import org.junit.Test;

import java.util.List;

import static gr.atrifyllis.devassignment.order.LineDto.convertOrderLinetoProductDto;
import static org.assertj.core.api.Assertions.assertThat;

public class LineDtoTest {

    @Test
    public void shouldConvertToDto() {
        List<Product> twoProducts = ProductSampleCreator.getTwoProducts();
        OrderLine l = new OrderLine(OrderSampleCreator.getOrder(twoProducts), twoProducts.get(0));
        LineDto lineDto = convertOrderLinetoProductDto().apply(l);

        assertThat(lineDto.getPrice()).isEqualTo(twoProducts.get(0).getCurrentPrice());
        assertThat(lineDto.getName()).isEqualTo(twoProducts.get(0).getName());
    }
}
