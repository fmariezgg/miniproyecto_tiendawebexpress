package ni.edu.uam.miniproyecto_tiendawebexpress.controller;

import ni.edu.uam.miniproyecto_tiendawebexpress.service.CategoriaService;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "catalogo";
    }
}