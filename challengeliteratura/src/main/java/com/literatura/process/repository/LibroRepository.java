package com.literatura.process.repository;
import com.literatura.process.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String nombre);
    Libro findByTituloContainsIgnoreCase(String nombre);
    List<Libro> findByIdioma(String idioma);
    @Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> findTop10ByTituloByDescargas();
}
