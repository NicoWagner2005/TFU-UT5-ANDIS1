package salasberrysilva.group6.tfu5.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import salasberrysilva.group6.tfu5.models.Carrito;
import salasberrysilva.group6.tfu5.models.EstadoPedido;
import salasberrysilva.group6.tfu5.models.Pedido;

class PedidoServiceTests {

    @Test
    void crearCarritoArrancaSinProductos() {
        PedidoService pedidoService = new PedidoService(new ProductService());

        Carrito carrito = pedidoService.crearCarrito("Nicolas");

        assertEquals(1, carrito.getId());
        assertEquals("Nicolas", carrito.getClientName());
        assertTrue(carrito.getProductos().isEmpty());
    }

    @Test
    void agregarProductoExistenteAlCarrito() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");

        Carrito carritoActualizado = pedidoService.agregarProducto(carrito.getId(), 1);

        assertEquals(1, carritoActualizado.getProductos().size());
        assertEquals("Burger", carritoActualizado.getProductos().get(0).name());
    }

    @Test
    void confirmarCarritoCreaPedidoConProductos() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");
        pedidoService.agregarProducto(carrito.getId(), 1);

        Pedido pedido = pedidoService.confirmarCarrito(carrito.getId());

        assertEquals(1, pedido.getId());
        assertEquals("Nicolas", pedido.getClientName());
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.getEstado());
        assertEquals(1, pedido.getProductos().size());
        assertEquals("Burger", pedido.getProductos().get(0).name());
    }

    @Test
    void pedidosConfirmadosTienenIdsDistintos() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito primerCarrito = pedidoService.crearCarrito("Nicolas");
        Carrito segundoCarrito = pedidoService.crearCarrito("Sofia");

        Pedido primerPedido = pedidoService.confirmarCarrito(primerCarrito.getId());
        Pedido segundoPedido = pedidoService.confirmarCarrito(segundoCarrito.getId());

        assertEquals(1, primerPedido.getId());
        assertEquals(2, segundoPedido.getId());
    }

    @Test
    void carritoInexistenteDaError() {
        PedidoService pedidoService = new PedidoService(new ProductService());

        assertThrows(ResponseStatusException.class, () -> pedidoService.agregarProducto(99, 1));
    }

    @Test
    void productoInexistenteDaError() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");

        assertThrows(ResponseStatusException.class, () -> pedidoService.agregarProducto(carrito.getId(), 99));
    }

    @Test
    void marcarPedidoListoYEntregarlo() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");
        Pedido pedido = pedidoService.confirmarCarrito(carrito.getId());

        pedidoService.marcarPedidoListo(pedido.getId());
        Pedido pedidoEntregado = pedidoService.entregarPedido(pedido.getId());

        assertEquals(EstadoPedido.ENTREGADO, pedidoEntregado.getEstado());
    }

    @Test
    void noEntregaPedidoQueNoEstaListo() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");
        Pedido pedido = pedidoService.confirmarCarrito(carrito.getId());

        assertThrows(ResponseStatusException.class, () -> pedidoService.entregarPedido(pedido.getId()));
    }

    @Test
    void cancelarPedidoLoDejaCancelado() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");
        Pedido pedido = pedidoService.confirmarCarrito(carrito.getId());

        Pedido pedidoCancelado = pedidoService.cancelarPedido(pedido.getId());

        assertEquals(EstadoPedido.CANCELADO, pedidoCancelado.getEstado());
    }

    @Test
    void noEntregaPedidoCancelado() {
        PedidoService pedidoService = new PedidoService(new ProductService());
        Carrito carrito = pedidoService.crearCarrito("Nicolas");
        Pedido pedido = pedidoService.confirmarCarrito(carrito.getId());
        pedidoService.cancelarPedido(pedido.getId());

        assertThrows(ResponseStatusException.class, () -> pedidoService.entregarPedido(pedido.getId()));
    }
}
