package gr.atrifyllis.devassignment.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<PlacedOrder, Long> {

    List<PlacedOrder> findByPlacedAtBetween(LocalDateTime after, LocalDateTime before);
}
