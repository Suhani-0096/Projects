package BhagyaSwapnJewels.Controller;

import BhagyaSwapnJewels.entity.Product;
import BhagyaSwapnJewels.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
// @CrossOrigin(
//         origins = "http://localhost:3000",
//         allowCredentials = "true"
// )
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Public: Get all active products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllActiveProducts();
    }

    // Public: Get a product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Owner only: Add a product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // Owner only: Edit an existing product
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        product.setId(id);
        return productService.updateProduct(id, product);
    }

    // Owner only: Delete a product
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
