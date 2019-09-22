package gr.atrifyllis.devassignment.order;

import gr.atrifyllis.devassignment.product.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderSampleCreator {

    public static PlacedOrder getOrder(Set<Product> persistedProducts) {
        return PlacedOrder.builder()
                .buyer("valid@email.com")
                .placedAt(LocalDateTime.now())
                .products(persistedProducts)
                .build();
    }


    static OrderDto getOrderDtoFromOrder(PlacedOrder order, List<Product> persistedProducts) {
        return OrderDto.builder()
                .buyer(order.getBuyer())
                .productIds(getIdsFromProducts(persistedProducts))
                .build();
    }

    static Set<Long> getIdsFromProducts(List<Product> persistedProducts) {
        return persistedProducts.stream().map(Product::getId).collect(Collectors.toSet());
    }
}
