package gr.atrifyllis.devassignment.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<PlacedOrder>> getOrders() {
        return ResponseEntity.ok().body(this.orderService.findAll());
    }


    /**
     * Creates a new order.
     *
     * @param order the new order details together with its product ids.
     * @return the created order.
     */
    @PostMapping
    public ResponseEntity<PlacedOrder> createOrder(@Valid @RequestBody OrderDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.create(order));
    }
}
