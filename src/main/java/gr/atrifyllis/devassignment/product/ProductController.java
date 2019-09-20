package gr.atrifyllis.devassignment.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
class ProductController {
    private ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieve all products.
     *
     * @return the list of Product details.
     */
    @GetMapping("/products")
    List<Product> getProducts() {
        return this.productService.findAll();
    }
}
