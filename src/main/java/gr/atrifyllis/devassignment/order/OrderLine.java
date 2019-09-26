package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * We need to map this intermediate table to an entity because we need
 * to store the price of the product in a specific point in time.
 * We don't need a new Id for this table so we use a composite id (productID, orderId).
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
class OrderLine {

    @EmbeddedId
    private OrderLineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private PlacedOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column
    private BigDecimal price;

    OrderLine(PlacedOrder order, Product product) {
        this.order = order;
        this.product = product;
        this.price = product.getCurrentPrice();
        this.id = new OrderLineId();
    }
}
