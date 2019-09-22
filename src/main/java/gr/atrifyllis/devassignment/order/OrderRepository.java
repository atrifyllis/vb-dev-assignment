package gr.atrifyllis.devassignment.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<PlacedOrder, Long> {

}
