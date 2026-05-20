package ni.edu.uam.miniproyecto_tiendawebexpress.service;

import jakarta.servlet.http.HttpSession;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.CarritoItem;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Categoria;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Producto;
import ni.edu.uam.miniproyecto_tiendawebexpress.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    private ProductoRepository productoRepository;
    private CarritoService carritoService;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        carritoService = new CarritoService(productoRepository);
        session = new MockHttpSession();
    }

    @Test
    void agregaProductoRespetandoStock() {
        Producto producto = crearProducto(1L, "Teclado", 2, new BigDecimal("10.00"));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        carritoService.agregarProducto(session, 1L, 1);
        carritoService.agregarProducto(session, 1L, 2);

        CarritoService.EstadoCarrito estado = carritoService.obtenerEstado(session);
        List<CarritoItem> items = estado.items();

        assertEquals(1, items.size());
        assertEquals(2, items.getFirst().getCantidad());
        assertEquals(new BigDecimal("20.00"), estado.total());
    }

    @Test
    void finalizarCompraDescuentaStockYLimpiaCarrito() {
        Producto producto = crearProducto(2L, "Mouse", 5, new BigDecimal("12.00"));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));

        carritoService.agregarProducto(session, 2L, 3);
        CarritoService.ResultadoOperacion resultado = carritoService.finalizarCompra(session);

        assertTrue(resultado.exito());
        assertEquals(2, producto.getStock());
        assertTrue(carritoService.obtenerEstado(session).items().isEmpty());
        verify(productoRepository, atLeastOnce()).save(producto);
    }

    private Producto crearProducto(Long id, String nombre, Integer stock, BigDecimal precio) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setCategoria(new Categoria());
        return producto;
    }
}

