package salasberrysilva.group6.tfu5.models;

import java.util.ArrayList;
import java.util.List;

public class Carrito {

    private int id;
    private String clientName;
    private List<Product> productos;

    public Carrito(int id, String clientName) {
        this.id = id;
        this.clientName = clientName;
        this.productos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public List<Product> getProductos() {
        return productos;
    }

    public void addProduct(Product product) {
        this.productos.add(product);
    }
}
