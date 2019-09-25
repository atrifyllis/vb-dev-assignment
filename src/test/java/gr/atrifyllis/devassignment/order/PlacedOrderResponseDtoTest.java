package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.ProductSampleCreator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class PlacedOrderResponseDtoTest {

    @Test
    public void shouldConvertToDto() {
        PlacedOrder order = OrderSampleCreator.getOrder(ProductSampleCreator.getTwoProducts());

        PlacedOrderResponseDto o = PlacedOrderResponseDto.convertToDto(order);

        assertThat(o.getId()).isEqualTo(order.getId());
        assertThat(o.getBuyer()).isEqualTo(order.getBuyer());
        assertThat(o.getPlacedAt()).isEqualTo(order.getPlacedAt());
        assertThat(o.getTotalPrice()).isEqualTo(new BigDecimal("101.54"));
        assertThat(o.getLineProducts()).hasSize(2);

    }
}
