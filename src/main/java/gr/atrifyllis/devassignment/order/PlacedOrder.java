package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class PlacedOrder {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String buyer;

    private LocalDateTime placedAt;

    @javax.persistence.Column(updatable = false) // TODO check if this extra precaution is needed
    @Setter(AccessLevel.PRIVATE)
    private BigDecimal orderPrice;

    @ManyToMany
    @JoinTable(name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    /**
     * This method should be called only on save and never again.
     */
    private void calculateTotal() {
        this.orderPrice = this.products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
