package ni.edu.uam.miniproyecto_tiendawebexpress.controller;

import jakarta.servlet.http.HttpSession;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        CarritoService.EstadoCarrito estado = carritoService.obtenerEstado(session);
        model.addAttribute("items", estado.items());
        model.addAttribute("total", estado.total());
        model.addAttribute("avisos", estado.avisos());
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarProducto(
            @RequestParam Long productoId,
            @RequestParam(defaultValue = "1") Integer cantidad,
            @RequestParam(defaultValue = "/catalogo") String redirectTo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        CarritoService.ResultadoOperacion resultado = carritoService.agregarProducto(session, productoId, cantidad);
        String flashKey = resultado.exito() ? "mensaje" : "error";
        redirectAttributes.addFlashAttribute(flashKey, resultado.mensaje());
        return "redirect:" + redirectTo;
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarProducto(
            @RequestParam Long productoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        CarritoService.ResultadoOperacion resultado = carritoService.eliminarProducto(session, productoId);
        String flashKey = resultado.exito() ? "mensaje" : "error";
        redirectAttributes.addFlashAttribute(flashKey, resultado.mensaje());
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/finalizar")
    public String finalizarPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        CarritoService.ResultadoOperacion resultado = carritoService.finalizarCompra(session);
        String flashKey = resultado.exito() ? "mensaje" : "error";
        redirectAttributes.addFlashAttribute(flashKey, resultado.mensaje());
        return "redirect:/carrito";
    }
}

