package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public
class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Retrieve list of all Orders.
     *
     * @return the list of Orders.
     */
    List<PlacedOrderResponseDto> findAll() {
        return this.orderRepository.findAll().stream()
                .map(PlacedOrderResponseDto::convertToDto)
                .collect(toList());
    }

    /**
     * Retrieve list of Orders inside Date range.
     *
     * @param createdBefore upper bound of range.
     * @param createdAfter  lower bound of range.
     * @return the list of Orders.
     */
    List<PlacedOrderResponseDto> findAll(LocalDate createdBefore, LocalDate createdAfter) {
        return this.orderRepository
                .findByPlacedAtBetween(createdAfter.atStartOfDay(), createdBefore.atTime(23, 59)).stream()
                .map(PlacedOrderResponseDto::convertToDto)
                .collect(toList());
    }

    /**
     * Creates a new order with specified products.
     * If one of the products does not exist the whole creation fails.
     *
     * @param o the new order details
     * @return the saved order details
     * @throws EntityNotFoundException if one of the Products is not found in the database.
     */
    PlacedOrderResponseDto create(OrderDto o) {
        List<Product> persistedProducts = o.productIds.stream()
                .map(pId -> this.productRepository.findById(pId)
                        .orElseThrow(() -> new EntityNotFoundException("Unable to find Product with id " + pId)))
                .collect(toList());
        PlacedOrder savedOrder = this.orderRepository.save(new PlacedOrder(o.getBuyer(), LocalDateTime.now(), persistedProducts));
        return PlacedOrderResponseDto.convertToDto(savedOrder);
    }

}
