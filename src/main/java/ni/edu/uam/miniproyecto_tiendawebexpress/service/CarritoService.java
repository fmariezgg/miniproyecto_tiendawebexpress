package ni.edu.uam.miniproyecto_tiendawebexpress.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.CarritoItem;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Producto;
import ni.edu.uam.miniproyecto_tiendawebexpress.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarritoService {

    private static final String CARRITO_SESSION_KEY = "CARRITO_SESSION_KEY";

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public record ResultadoOperacion(boolean exito, String mensaje) {}

    public record EstadoCarrito(List<String> avisos, BigDecimal total, List<CarritoItem> items) {}

    public ResultadoOperacion agregarProducto(HttpSession session, Long productoId, Integer cantidadSolicitada) {
        if (productoId == null) {
            return new ResultadoOperacion(false, "Producto invalido.");
        }

        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isEmpty()) {
            return new ResultadoOperacion(false, "El producto no existe.");
        }

        Producto producto = productoOpt.get();
        int stockActual = Math.max(0, producto.getStock());
        if (stockActual == 0) {
            return new ResultadoOperacion(false, "No hay stock disponible para " + producto.getNombre() + ".");
        }

        int cantidad = (cantidadSolicitada == null || cantidadSolicitada < 1) ? 1 : cantidadSolicitada;
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);

        CarritoItem item = carrito.get(producto.getId());
        int cantidadBase = item != null ? item.getCantidad() : 0;
        int cantidadFinal = Math.min(stockActual, cantidadBase + cantidad);

        if (item == null) {
            item = new CarritoItem(producto.getId(), producto.getNombre(), producto.getImagen(), producto.getPrecio(), cantidadFinal);
            carrito.put(producto.getId(), item);
        } else {
            item.setNombre(producto.getNombre());
            item.setImagen(producto.getImagen());
            item.setPrecioUnitario(producto.getPrecio());
            item.setCantidad(cantidadFinal);
        }

        if (cantidadBase + cantidad > stockActual) {
            return new ResultadoOperacion(true, "Cantidad ajustada al stock disponible para " + producto.getNombre() + ".");
        }

        return new ResultadoOperacion(true, "Producto agregado al carrito.");
    }

    public ResultadoOperacion eliminarProducto(HttpSession session, Long productoId) {
        if (productoId == null) {
            return new ResultadoOperacion(false, "Producto invalido.");
        }

        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        CarritoItem eliminado = carrito.remove(productoId);
        if (eliminado == null) {
            return new ResultadoOperacion(false, "El producto no estaba en el carrito.");
        }

        return new ResultadoOperacion(true, "Producto eliminado del carrito.");
    }

    public EstadoCarrito obtenerEstado(HttpSession session) {
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        List<String> avisos = new ArrayList<>();

        List<Long> idsAEliminar = new ArrayList<>();
        for (Map.Entry<Long, CarritoItem> entry : carrito.entrySet()) {
            CarritoItem item = entry.getValue();
            Optional<Producto> productoOpt = productoRepository.findById(item.getProductoId());
            if (productoOpt.isEmpty()) {
                idsAEliminar.add(entry.getKey());
                avisos.add("Se removio un producto porque ya no existe.");
                continue;
            }

            Producto producto = productoOpt.get();
            int stock = Math.max(0, producto.getStock());
            if (stock == 0) {
                idsAEliminar.add(entry.getKey());
                avisos.add("Se removio " + producto.getNombre() + " porque esta agotado.");
                continue;
            }

            item.setNombre(producto.getNombre());
            item.setImagen(producto.getImagen());
            item.setPrecioUnitario(producto.getPrecio());
            if (item.getCantidad() > stock) {
                item.setCantidad(stock);
                avisos.add("Se ajusto la cantidad de " + producto.getNombre() + " por disponibilidad.");
            }
        }

        idsAEliminar.forEach(carrito::remove);
        List<CarritoItem> items = new ArrayList<>(carrito.values());
        return new EstadoCarrito(avisos, calcularTotal(items), items);
    }

    @Transactional
    public ResultadoOperacion finalizarCompra(HttpSession session) {
        EstadoCarrito estado = obtenerEstado(session);
        if (estado.items().isEmpty()) {
            return new ResultadoOperacion(false, "Tu carrito esta vacio.");
        }

        for (CarritoItem item : estado.items()) {
            Optional<Producto> productoOpt = productoRepository.findById(item.getProductoId());
            if (productoOpt.isEmpty()) {
                return new ResultadoOperacion(false, "No se pudo finalizar: un producto ya no existe.");
            }

            Producto producto = productoOpt.get();
            if (producto.getStock() < item.getCantidad()) {
                return new ResultadoOperacion(false, "Stock insuficiente para " + producto.getNombre() + ".");
            }
        }

        for (CarritoItem item : estado.items()) {
            Producto producto = productoRepository.findById(item.getProductoId()).orElseThrow();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        limpiar(session);
        return new ResultadoOperacion(true, "Pedido finalizado correctamente. Gracias por tu compra.");
    }

    public void limpiar(HttpSession session) {
        session.removeAttribute(CARRITO_SESSION_KEY);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CarritoItem> obtenerCarrito(HttpSession session) {
        Object carritoSession = session.getAttribute(CARRITO_SESSION_KEY);
        if (carritoSession instanceof Map<?, ?> carritoMap) {
            return (Map<Long, CarritoItem>) carritoMap;
        }

        Map<Long, CarritoItem> nuevoCarrito = new LinkedHashMap<>();
        session.setAttribute(CARRITO_SESSION_KEY, nuevoCarrito);
        return nuevoCarrito;
    }

    private BigDecimal calcularTotal(List<CarritoItem> items) {
        return items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

