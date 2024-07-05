package com.literatura.process.models;
import com.literatura.process.dto.DatosAutor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer cumple;
    private Integer fallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros;
    public Autor() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCumple() {
        return cumple;
    }

    public Integer getFallecimiento() {
        return fallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.cumple = autor.cumple();
        this.fallecimiento = autor.fallecimiento();
    }

    @Override
    public String toString() {
        return
                "nombre = '" + nombre + '\'' +
                ", cumple = " + cumple +
                ", fallecimiento = " + fallecimiento;
    }
}
