package salasberrysilva.group6.tfu5.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import salasberrysilva.group6.tfu5.models.Carrito;
import salasberrysilva.group6.tfu5.models.EstadoPedido;
import salasberrysilva.group6.tfu5.models.Pedido;
import salasberrysilva.group6.tfu5.models.Product;

@Service
public class PedidoService {

    private final ProductService productService;
    private final Map<Integer, Carrito> carritos = new HashMap<>();
    private final Map<Integer, Pedido> pedidos = new HashMap<>();
    private int nextPedidoId = 1;
    private int nextCarritoId = 1;

    public PedidoService(ProductService productService) {
        this.productService = productService;
    }

    public Carrito crearCarrito(String clientName) {
        Carrito carrito = new Carrito(nextCarritoId++, clientName);
        carritos.put(carrito.getId(), carrito);
        return carrito;
    }

    public Carrito agregarProducto(int carritoId, int productId) {
        Carrito carrito = getCarrito(carritoId);
        Product product = productService.getProductById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        carrito.addProduct(product);
        return carrito;
    }

    public Pedido confirmarCarrito(int carritoId) {
        Carrito carrito = getCarrito(carritoId);
        Pedido pedido = crearPedido(carrito.getClientName());

        for (Product product : carrito.getProductos()) {
            pedido.addProduct(product);
        }

        carritos.remove(carritoId);
        return pedido;
    }

    public Pedido crearPedido(String clientName) {
        Pedido pedido = new Pedido(nextPedidoId++, clientName);
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    private Carrito getCarrito(int carritoId) {
        Carrito carrito = carritos.get(carritoId);

        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }

        return carrito;
    }

    public Pedido cancelarPedido(int pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);
        pedido.cancelar();
        return pedido;
    }

    public Pedido obtenerPedido(int pedidoId) {
        Pedido pedido = pedidos.get(pedidoId);

        if (pedido == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }

        return pedido;
    }

    public Map<Integer, Pedido> obtenerTodosPedidos() {
        return new HashMap<>(pedidos);
    }

    public Pedido marcarPedidoListo(int pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);
        pedido.marcarListo();
        return pedido;
    }

    public Pedido entregarPedido(int pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede entregar un pedido cancelado");
        }

        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden entregar pedidos listos");
        }

        pedido.marcarEntregado();
        return pedido;
    }

    public double calcularTotal(int pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);
        return pedido.calcularTotal();
    }
}
