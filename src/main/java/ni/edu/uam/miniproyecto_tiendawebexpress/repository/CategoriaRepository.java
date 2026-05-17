package ni.edu.uam.miniproyecto_tiendawebexpress.repository;

import ni.edu.uam.miniproyecto_tiendawebexpress.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}