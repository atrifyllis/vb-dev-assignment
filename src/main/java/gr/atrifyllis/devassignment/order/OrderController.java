package gr.atrifyllis.devassignment.order;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieve all orders.
     *
     * @return the list of order details together with its product details.
     */
    @GetMapping
    public ResponseEntity<List<PlacedOrderResponseDto>> getOrders(
            @RequestParam(required = false, name = "created_before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore,
            @RequestParam(required = false, name = "created_after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAfter
    ) {
        return ResponseEntity.ok().body(
                createdBefore != null && createdAfter != null ?
                        this.orderService.findAll(createdBefore, createdAfter) :
                        this.orderService.findAll()
        );
    }


    /**
     * Creates a new order.
     *
     * @param order the new order details together with its product ids.
     * @return the created order.
     */
    @PostMapping
    public ResponseEntity<PlacedOrderResponseDto> createOrder(@Valid @RequestBody OrderDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.create(order));
    }
}
