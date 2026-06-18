package salasberrysilva.group6.tfu5.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import salasberrysilva.group6.tfu5.services.ProductService;
import salasberrysilva.group6.tfu5.models.Product;

@RestController
public class RegistrarPedidoController {

    private final ProductService productService;

    public RegistrarPedidoController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }
}
