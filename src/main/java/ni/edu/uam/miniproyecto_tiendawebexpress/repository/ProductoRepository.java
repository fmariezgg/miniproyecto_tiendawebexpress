package ni.edu.uam.miniproyecto_tiendawebexpress.repository;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Id(Long categoriaId);
}