package salasberrysilva.group6.tfu5.models;

import java.util.List;
import java.util.ArrayList;

public class Pedido {

    private int id;
    private String clientName;
    private List<Product> productos;
    private EstadoPedido estado;

    public Pedido(int id, String clientName) {
        this.id = id;
        this.clientName = clientName;
	this.productos = new ArrayList<>();
        this.estado = EstadoPedido.EN_PREPARACION;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<Product> getProductos() {
        return productos;
    }

    public void marcarListo() {
        this.estado = EstadoPedido.LISTO;
    }

    public void addProduct(Product product) {
        this.productos.add(product);
    }
}
