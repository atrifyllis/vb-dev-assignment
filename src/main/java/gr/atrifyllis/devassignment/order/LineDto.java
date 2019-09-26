package gr.atrifyllis.devassignment.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Used both as a DTO for Product creation and for the representation of a Line Product.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class LineDto {
    /**
     * The name of the product.
     */
       String name;
    /**
     * The price of the product.
     */
      BigDecimal price;

    public static Function<OrderLine, LineDto> convertOrderLinetoProductDto() {
        return p -> LineDto.builder()
                .price(p.getPrice())
                .name(p.getProduct().getName())
                .build();
    }

}
