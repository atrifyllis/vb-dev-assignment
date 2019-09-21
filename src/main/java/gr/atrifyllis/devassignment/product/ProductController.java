package gr.atrifyllis.devassignment.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("products")
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
    @GetMapping
    ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok().body(this.productService.findAll());
    }

    /**
     * Creates a new product.
     *
     * @param product the new product details.
     * @return the created product.
     */
    @PostMapping
    ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.create(product));
    }
}
