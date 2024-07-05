package com.literatura.process.repository;
import com.literatura.process.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    List<Autor> findByCumpleLessThanOrFallecimientoGreaterThanEqual(int fecha, int fechaa);
    Optional<Autor> findFirstByNombreContainsIgnoreCase(String escritor);
}
