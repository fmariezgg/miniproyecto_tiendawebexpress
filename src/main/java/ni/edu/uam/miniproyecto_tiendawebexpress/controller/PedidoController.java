package ni.edu.uam.miniproyecto_tiendawebexpress.controller;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.DetallePedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Pedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.model.Producto;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.PedidoService;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/formulario")
    public String formularioPedido(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        return "pedido-formulario";
    }

    @PostMapping("/crear")
    public String crearPedido(
            @RequestParam String nombreCliente,
            @RequestParam String correo,
            @RequestParam String comentario,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            Model model) {

        try {
            // Crear el pedido base
            Pedido pedido = pedidoService.crearPedido(nombreCliente, correo, comentario);

            // Obtener el producto
            Producto producto = productoService.obtenerPorId(productoId).orElse(null);

            if (producto != null) {
                // Crear detalle del pedido
                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(producto.getPrecio().multiply(new BigDecimal(cantidad)));

                // Inicializar lista de detalles
                if (pedido.getDetalles() == null) {
                    pedido.setDetalles(new ArrayList<>());
                }
                pedido.getDetalles().add(detalle);

                // Calcular total
                BigDecimal total = producto.getPrecio().multiply(new BigDecimal(cantidad));
                pedido.setTotal(total);

                // Guardar el pedido con el detalle
                pedidoService.guardar(pedido);
            }

            model.addAttribute("pedido", pedido);
            return "pedido-resumen";

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el pedido: " + e.getMessage());
            model.addAttribute("productos", productoService.obtenerTodos());
            return "pedido-formulario";
        }
    }

    @GetMapping("/{id}")
    public String verPedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.obtenerPorId(id).orElse(null));
        return "pedido-resumen";
    }
}