package gr.atrifyllis.devassignment.order;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class OrderService {
    private OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    List<PlacedOrder> findAll() {
        return this.orderRepository.findAll();
    }

    PlacedOrder create(PlacedOrder order) {
        return this.orderRepository.save(order);
    }
}
