package gr.atrifyllis.devassignment.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
class OrderLineId implements Serializable {

    @Column
    private Long orderId;

    @Column
    private Long productId;
}
