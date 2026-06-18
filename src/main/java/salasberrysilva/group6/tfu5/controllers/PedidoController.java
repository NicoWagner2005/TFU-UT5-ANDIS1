package salasberrysilva.group6.tfu5.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import salasberrysilva.group6.tfu5.models.Carrito;
import salasberrysilva.group6.tfu5.models.Pedido;
import salasberrysilva.group6.tfu5.services.PedidoService;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/carrito")
    public Carrito crearCarrito(@RequestParam String clientName) {
        return pedidoService.crearCarrito(clientName);
    }

    @PostMapping("/carrito/{carritoId}/productos/{productId}")
    public Carrito agregarProducto(@PathVariable int carritoId, @PathVariable int productId) {
        return pedidoService.agregarProducto(carritoId, productId);
    }

    @PostMapping("/carrito/{carritoId}/confirmar")
    public Pedido confirmarCarrito(@PathVariable int carritoId) {
        return pedidoService.confirmarCarrito(carritoId);
    }

    @GetMapping("/pedidos/{pedidoId}")
    public Pedido obtenerPedido(@PathVariable int pedidoId) {
        return pedidoService.obtenerPedido(pedidoId);
    }

    @GetMapping("/pedidos")
    public java.util.Map<Integer, Pedido> obtenerTodosPedidos() {
        return pedidoService.obtenerTodosPedidos();
    }

    @PostMapping("/pedidos/{pedidoId}/marcar-listo")
    public Pedido marcarPedidoListo(@PathVariable int pedidoId) {
        return pedidoService.marcarPedidoListo(pedidoId);
    }
}
