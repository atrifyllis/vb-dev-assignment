package gr.atrifyllis.devassignment.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
class OrderDto {
    @NotEmpty(message = "Please provide the email of the buyer")
    @Email
    String buyer;
    @NotEmpty(message = "Please provide the products for this order")
    Set<Long> productIds;
}
