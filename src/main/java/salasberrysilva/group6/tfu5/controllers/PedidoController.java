package salasberrysilva.group6.tfu5.controllers;

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
}
