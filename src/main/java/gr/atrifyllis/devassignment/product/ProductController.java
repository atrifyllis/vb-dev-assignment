package gr.atrifyllis.devassignment.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("products")
class ProductController {

    private final ProductService productService;

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

    /**
     * Updates an existing product.
     *
     * @param product the product details that.
     * @return the created product.
     */
    @PutMapping("/{id}")
    ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto product) {
        return ResponseEntity.ok().body(this.productService.update(id, product));
    }
}
