package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
public class PlacedOrder {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty
    @Email
    private String buyer;

    @PastOrPresent
    private LocalDateTime placedAt;

    /**
     * Maps to {@link OrderLine}s not to {@link Product}s (
     * This way a "snapshot" of the product at the time of the order is saved.
     */
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderLine> products = new ArrayList<>();

    /**
     * Creates OrderLines from Products for the new Order.
     *
     * @param buyer    the email of the buyer.
     * @param placedAt the timestamp of the order creation.
     * @param products the list of {@link Product}s for the order.
     */
    PlacedOrder(String buyer, LocalDateTime placedAt, List<Product> products) {
        this.buyer = buyer;
        this.placedAt = placedAt;
        this.products.addAll(
                products.stream()
                        .map(p -> new OrderLine(this, p))
                        .collect(Collectors.toList())
        );
    }
}
