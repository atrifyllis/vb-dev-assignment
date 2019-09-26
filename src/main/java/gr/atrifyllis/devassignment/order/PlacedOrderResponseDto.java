package gr.atrifyllis.devassignment.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static gr.atrifyllis.devassignment.order.LineDto.convertOrderLinetoProductDto;
import static java.util.stream.Collectors.toList;

@Data
@Builder
class PlacedOrderResponseDto {
    /**
     * The id of the order.
     */
    private Long id;
    /**
     * The buyer's email.
     */
    private String buyer;
    /**
     * A list of Line Products (that is the info of a Product in a specific point in time).
     */
    private List<LineDto> lineProducts;
    /**
     * The total price of all products of the order.
     */
    private BigDecimal totalPrice;

    /**
     * The timestamp when the order has been placed.
     */
    private LocalDateTime placedAt;

    static PlacedOrderResponseDto convertToDto(PlacedOrder o) {
        return PlacedOrderResponseDto.builder()
                .id(o.getId())
                .buyer(o.getBuyer())
                .lineProducts(o.getProducts().stream().map(convertOrderLinetoProductDto()).collect(toList()))
                .totalPrice(calculateTotalOrderPrice(o.getProducts().stream().map(OrderLine::getPrice).collect(toList())))
                .placedAt(o.getPlacedAt())
                .build();
    }

    /**
     * Calculates the total price of the order.
     * The total price of the order is a calculated field (not persisted).
     */
    static BigDecimal calculateTotalOrderPrice(List<BigDecimal> prices) {
        return prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
