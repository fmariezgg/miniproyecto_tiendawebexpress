package ni.edu.uam.miniproyecto_tiendawebexpress.service;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.Pedido;
import ni.edu.uam.miniproyecto_tiendawebexpress.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

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
}