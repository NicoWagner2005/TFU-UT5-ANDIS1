package salasberrysilva.group6.tfu5.services;

import org.springframework.stereotype.Service;
import salasberrysilva.group6.tfu5.models.Product;
import java.util.List;

@Service
public class ProductService {

    public List<Product> getProducts() {
        return List.of(
            new Product(1, "Burger", 250),
            new Product(2, "Fries", 120)
        );
    }
}
