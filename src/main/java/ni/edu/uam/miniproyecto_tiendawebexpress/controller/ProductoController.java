package ni.edu.uam.miniproyecto_tiendawebexpress.controller;

import ni.edu.uam.miniproyecto_tiendawebexpress.service.CategoriaService;
import ni.edu.uam.miniproyecto_tiendawebexpress.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerPorId(id).orElse(null));
        return "producto-detalle";
    }

    @GetMapping("/categoria/{categoriaId}")
    public String porCategoria(@PathVariable Long categoriaId, Model model) {
        model.addAttribute("productos", productoService.obtenerPorCategoria(categoriaId));
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "catalogo";
    }
}