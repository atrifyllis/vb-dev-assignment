package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    List<PlacedOrder> findAll() {
        return this.orderRepository.findAll();
    }

    PlacedOrder create(OrderDto o) {
        return this.orderRepository.save(PlacedOrder.builder()
                .buyer(o.getBuyer())
                .placedAt(LocalDateTime.now())
                .products(this.productRepository.findByIdIn(o.productIds))
                .build());
    }
}
