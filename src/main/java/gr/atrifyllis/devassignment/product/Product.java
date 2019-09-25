package gr.atrifyllis.devassignment.product;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * The product details.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Product {

    /**
     * The database id of the product.
     */
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product.
     */
    @Column(precision = 7, scale = 2)
    private BigDecimal currentPrice;

    @Builder
    public Product(String name, BigDecimal currentPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
    }
}
