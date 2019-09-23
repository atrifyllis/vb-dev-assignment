package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;
import gr.atrifyllis.devassignment.product.ProductRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Product> persistedProducts = o.productIds.stream()
                .map(pId -> this.productRepository.findById(pId).orElseThrow(() -> new EntityNotFoundException("Unable to find Product with id " + pId)))
                .collect(Collectors.toSet());
        return this.orderRepository.save(PlacedOrder.builder()
                .buyer(o.getBuyer())
                .placedAt(LocalDateTime.now())
                .products(persistedProducts)
                .build());
    }
}
