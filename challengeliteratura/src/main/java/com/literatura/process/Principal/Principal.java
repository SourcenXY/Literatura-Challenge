package com.literatura.process.Principal;
import com.literatura.process.service.ConsumoAPI;
import com.literatura.process.service.Conversion;
import com.literatura.process.models.Autor;
import com.literatura.process.models.Libro;
import com.literatura.process.models.LibrosDevueltos;
import com.literatura.process.dto.DatosLibro;
import com.literatura.process.repository.AutorRepository;
import com.literatura.process.repository.LibroRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private Conversion convertir = new Conversion();
    private static String url = "https://gutendex.com/books/?search=";
    private List<Libro> datosLibro = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu(){
        var opcion = -1;
        while (opcion != 8){
            var menu = """
                    Bienvenido a nuestra libreria
                    Ingrese una opcion del siguiente menu
                    
                    1. Agregar Libro por su nombre
                    2. Libros buscados
                    3. Buscar libro por su nombre
                    4. Buscar todos los Autores de libros buscados
                    5. Buscar Autores por año
                    6. Buscar Libros por su idioma
                    7. Buscar Autor por su nombre
                    8. Salir
                    """;
            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Ingrese una opcion valida del menu");
                teclado.nextLine();
                continue;
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroNombre();
                    break;
                case 4:
                    BuscarAutores();
                    break;
                case 5:
                    buscarAutoresFecha();
                    break;
                case 6:
                    buscarLibrosIdioma();
                    break;
                case 7:
                    buscarAutorNombre();
                    break;
                case 8:
                    System.out.println("Gracias por usar nuestro programa, cerrando programa...");
                    break;
                default:
                    System.out.println("Opcion invalida, ingrese una opcion del menu");
                    break;
            }
        }
    }

    private Libro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = teclado.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(url + nombreLibro.replace(" ", "%20"));
        LibrosDevueltos datos = convertir.convertirDatos(json, LibrosDevueltos.class);
            if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
                DatosLibro libreDevuelto = datos.getResultadoLibros().get(0);
                return new Libro(libreDevuelto);
            } else {
                System.out.println("No se encontro el libro");
                return null;
            }
    }

    private void buscarLibro() {
        Libro libro = getDatosLibro();
        if (libro == null){
            System.out.println("No se encontro el libro");
            return;
        }
        try{
            boolean libroEncontrado = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroEncontrado){
                System.out.println("El libro ya fue registrado");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("No se puede registrar el libro");
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontro el libro");
        } else {
            System.out.println("Se encontraron los siguientes libros: \n");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarLibroNombre() {
        System.out.println("Ingrese el nombre del libro: ");
        var libroNombre = teclado.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(libroNombre);
        if (libroBuscado != null) {
            System.out.println("El libro es: " + libroBuscado);
        } else {
            System.out.println("No se encontro el libro");
        }
    }

    private  void BuscarAutores(){
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No se encontro el autor\n");
        } else {
            System.out.println("Se encontraron los siguientes autores: \n");
            Set<String> autoresRegistrados = new HashSet<>();
            for (Autor autor : autores) {
                if (autoresRegistrados.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }

    private void  buscarLibrosIdioma(){
        System.out.println("Ingrese el idioma: ");
        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros con el idioma de " + idioma + "\n");
        } else {
            System.out.println("Se encontraron los siguientes libros con el idioma de " + idioma + ": \n");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }

    }

    private void buscarAutoresFecha() {
        System.out.println("Ingrese una fecha: \n");
        var fecha = teclado.nextInt();
        teclado.nextLine();
        List<Autor> fechaIngresada = autorRepository.findByCumpleLessThanOrFallecimientoGreaterThanEqual(fecha, fecha);
        if (fechaIngresada.isEmpty()) {
            System.out.println("No se encotraron autores con la fecha de " + fecha + ".");
        } else {
            System.out.println("Se encontraron los siguientes autores con la fecha de " + fecha + " son:");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : fechaIngresada) {
                if (autor.getCumple() != null && autor.getFallecimiento() != null) {
                    if (autor.getCumple() <= fecha && autor.getFallecimiento() >= fecha) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

    private void buscarAutorNombre() {
        System.out.println("Ingrese nombre del autor: ");
        var autor = teclado.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(autor);
        if (escritorBuscado != null) {
            System.out.println("\nSe encontro el autor con el nombre de: " + escritorBuscado.get().getNombre());
        } else {
            System.out.println("\nEl autor con el nombre de '" + autor + "' no se encontró");
        }
    }
}
