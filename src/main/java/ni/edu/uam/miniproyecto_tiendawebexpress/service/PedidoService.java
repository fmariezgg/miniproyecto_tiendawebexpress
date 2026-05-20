package ni.edu.uam.miniproyecto_tiendawebexpress.service;

import jakarta.transaction.Transactional;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.DetallePedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Pedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Producto;
import ni.edu.uam.miniproyecto_tiendawebexpress.repository.PedidoRepository;
import ni.edu.uam.miniproyecto_tiendawebexpress.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido crearPedido(String nombreCliente, String correo, String comentario) {
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(nombreCliente);
        pedido.setCorreo(correo);
        pedido.setComentario(comentario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.ZERO);
        return pedidoRepository.save(pedido);
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Transactional
    public Pedido crearPedidoConDetalle(String nombreCliente, String correo, String comentario, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto seleccionado no existe."));

        if (producto.getStock() <= 0) {
            throw new IllegalArgumentException("El producto esta agotado.");
        }

        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(nombreCliente);
        pedido.setCorreo(correo);
        pedido.setComentario(comentario);
        pedido.setFechaPedido(LocalDateTime.now());

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));

        pedido.setDetalles(new ArrayList<>());
        pedido.getDetalles().add(detalle);
        pedido.setTotal(detalle.getSubtotal());

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        return pedidoRepository.save(pedido);
    }
}