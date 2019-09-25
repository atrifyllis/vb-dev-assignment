package gr.atrifyllis.devassignment.order;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderLineId implements Serializable {

    @Column
    private Long orderId;

    @Column
    private Long productId;
}
