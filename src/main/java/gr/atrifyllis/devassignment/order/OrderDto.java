package gr.atrifyllis.devassignment.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * The order details used for the order creation.
 */
@Data
@Builder
class OrderDto {
    /**
     * The buyer's email.
     */
    @NotEmpty(message = "Please provide the email of the buyer")
    @Email
    String buyer;
    /**
     * The list of product Ids ordered.
     */
    @NotEmpty(message = "Please provide the products for this order")
    Set<Long> productIds;
}
