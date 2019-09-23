package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
class PlacedOrder {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty
    @Email
    private String buyer;

    @PastOrPresent
    private LocalDateTime placedAt;

    @Column(updatable = false, precision = 7, scale = 2) // TODO check if this extra precaution is needed
    @Setter(AccessLevel.NONE)
    private BigDecimal orderPrice;

    // Set is used instead of List because of delete operation performance  issues.
    @ManyToMany
    @JoinTable(name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @Builder // builder annotates constructor to exclude specific fields
    public PlacedOrder(String buyer, LocalDateTime placedAt, Set<Product> products) {
        this.buyer = buyer;
        this.placedAt = placedAt;
        this.products = products;
    }

    /**
     * This method should be called only on save and never again.
     */
    @PrePersist
    private void calculateTotal() {
        this.orderPrice = this.products.stream()
                .map(Product::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
