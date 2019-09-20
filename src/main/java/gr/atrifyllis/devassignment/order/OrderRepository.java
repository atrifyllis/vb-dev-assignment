package gr.atrifyllis.devassignment.order;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<PlacedOrder, Long> {

}
