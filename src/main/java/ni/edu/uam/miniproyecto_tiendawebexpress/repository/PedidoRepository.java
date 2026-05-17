package ni.edu.uam.miniproyecto_tiendawebexpress.repository;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
