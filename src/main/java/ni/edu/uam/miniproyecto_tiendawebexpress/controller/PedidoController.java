package ni.edu.uam.miniproyecto_tiendawebexpress.controller;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.Pedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.PedidoService;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/formulario")
    public String formularioPedido(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Integer cantidad,
            Model model) {

        cargarDatosFormulario(model, productoId, cantidad);
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
            Pedido pedido = pedidoService.crearPedidoConDetalle(nombreCliente, correo, comentario, productoId, cantidad);
            model.addAttribute("pedido", pedido);
            return "pedido-resumen";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarDatosFormulario(model, productoId, cantidad);
            model.addAttribute("nombreCliente", nombreCliente);
            model.addAttribute("correo", correo);
            model.addAttribute("comentario", comentario);
            return "pedido-formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el pedido: " + e.getMessage());
            cargarDatosFormulario(model, productoId, cantidad);
            return "pedido-formulario";
        }
    }

    @GetMapping("/{id}")
    public String verPedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.obtenerPorId(id).orElse(null));
        return "pedido-resumen";
    }

    private void cargarDatosFormulario(Model model, Long productoId, Integer cantidad) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("productoSeleccionadoId", productoId);
        model.addAttribute("cantidadSeleccionada", (cantidad != null && cantidad > 0) ? cantidad : 1);
    }
}