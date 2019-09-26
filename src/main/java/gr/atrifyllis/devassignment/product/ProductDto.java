package gr.atrifyllis.devassignment.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Used both as a DTO for Product creation and for the representation of a Line Product.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public
class ProductDto {
    /**
     * The name of the product.
     */
    @NotEmpty(message = "Please provide a name for the product")
    String name;
    /**
     * The price of the product.
     */
    @NotNull(message = "Please provide a price for the product")
    @DecimalMin("1.00")
    @Digits(integer = 7, fraction = 2)
    BigDecimal price;
}
