package salasberrysilva.group6.tfu5.services;

import org.springframework.stereotype.Service;
import salasberrysilva.group6.tfu5.models.Product;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private int nextId = 1;

    private final List<Product> products = List.of(
        createProduct("Burger", 250),
        createProduct("Fries", 120)
    );

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> getProductById(int id) {
        return products.stream()
            .filter(product -> product.id() == id)
            .findFirst();
    }

    private Product createProduct(String name, int price) {
        return new Product(nextId++, name, price);
    }
}
