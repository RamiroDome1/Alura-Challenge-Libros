package Alura_Challenge.demo.principal;

import Alura_Challenge.demo.model.ApiConsumer;
import Alura_Challenge.demo.model.Libro;
import Alura_Challenge.demo.repository.LibroRepository;
import Alura_Challenge.demo.servises.ApiRequest;
import Alura_Challenge.demo.servises.ConvierteDatos;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Component
public class Principal {
    @Autowired  // Inyecta el repositorio automáticamente
    private LibroRepository libroRepository;
    Scanner teclado = new Scanner(System.in);
    String URL_BASE = "https://gutendex.com/books/?";
    String URL_BUSQUEDA = "search=";
    ConvierteDatos conversor = new ConvierteDatos();
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Agregar libro📚
                    2 - Mostrar todos los libros📖🔍
                    3 - Mostrar libros en un lenguaje especifico🌎
                    4 - Mostrar libros de autores vivos en x fecha⏰
                    5 - Mostrar todos los autores👴
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Escriba el libro que quiera agregar");
                    agregarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    System.out.println("Ingrese el idioma por el cual quiere filtar: es, en");
                    String idioma1 = teclado.nextLine();
                    String idioma = idioma1;
                    mostrarLibrosPorIdioma(idioma);

                    break;
                case 4:
                    System.out.println("Ingrese el ano que quiere ver");
                    int libro1 = teclado.nextInt();
                    int libro = libro1;
                    mostrarLibrosDesdeFecha(libro);
                    break;
                case 5:
                    mostrarAutores();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }
    //METODO PARA CONSEGUIR LOS DATOS DE LA API
    private ApiConsumer getDatosLibro() {
        var nombreLibro = teclado.nextLine();
        var json = ApiRequest.realizarRequest(URL_BASE + URL_BUSQUEDA +nombreLibro.replace(" ", "+").toLowerCase() );
        ApiConsumer datos = conversor.obtenerDatos(json, ApiConsumer.class);
        return datos;
    }

    //METODO PARA CAMBIAR DE JSON A OBJETO JAVA Y POR ULTIMO IMPRIMIR EL LIBRO Y SUBIRLO A LA BASE DE DATOS
    private void agregarLibro() {
        ApiConsumer datosLibro = getDatosLibro();
        if (datosLibro.results().isEmpty()) {
            System.out.println("❌ No se encontró el libro en la API");
            return;
        }

        ApiConsumer.LibroDto libroApi = datosLibro.results().get(0);
        String nombreAutor = libroApi.authors().isEmpty() ? "Desconocido"
                : libroApi.authors().get(0).name();


        Integer birthYear = libroApi.authors().isEmpty() ? null : libroApi.authors().get(0).birth_year();
        Integer deathYear = libroApi.authors().isEmpty() ? null : libroApi.authors().get(0).death_year();


        //CREAMOS EL LIBRO QUE VAMOS A AGREGAR
        Libro libro = new Libro(
                libroApi.title(),
                libroApi.languages(),
                libroApi.download_count(),
                nombreAutor,
                birthYear,
                deathYear
        );

        //VERIFICAMOS SI ESTA EN LA BASE DE DATOS SI ESTA NO SE GUARDA
        List<Libro> libros = libroRepository.findAll();
        boolean existe = libros.stream().anyMatch(libro1 -> libro1.getTitle().equals(libro.getTitle()));
        if (!existe) {
            libroRepository.save(libro);
            System.out.println("✅ Libro guardado: " + libro.getTitle());
        } else {
            System.out.println("❌ El libro ya existe: " + libro.getTitle());
        }
    }

    //METODO PARA TRAER TODOS LOS LIBROS DE LA BASE DE DATOS
    private void mostrarLibros() {
        List<Libro> libros = libroRepository.findAll(); // Obtiene todos los libros

        if (libros.isEmpty()) {
            System.out.println("❌ No hay libros en la base de datos");
            return;
        }

        System.out.println("\n📚 Libros en la base de datos:");
        libros.forEach(System.out::println); // Imprime cada libro usando su toString()
        return;
    }

    //METODO PARA MOSTRAR TODOS LOS AUTORES
    private void mostrarAutores(){
        List<Libro> libros = libroRepository.findAll(); // Obtiene todos los libros

        if (libros.isEmpty()) {
            System.out.println("❌ No hay libros en la base de datos");
            return;
        }

        System.out.println("\n👴 Autores en la base de datos:");
        libros.forEach(libro -> System.out.println("Autor: "+ libro.getAuthorName()+"\n"
                +"Fecha de nacimiento: "+libro.getDeath_year()+"\n"
                + "Fecha de fallecimiento: "+libro.getBirth_year()+"\n")); // Imprime cada libro usando su toString()
        return;
    }

    //METODO PARA MOSTRAR LIBROS POR FECHA Y LOS AUTORES QUE VIVIERON EN ESA FECHA
    private void mostrarLibrosDesdeFecha(Integer fecha) {
        List<Libro> libros = libroRepository.findAll();
        List<Libro> librosFiltrados  = libros.stream()
                .filter(libro -> libro.getBirth_year()<=fecha)
                .filter(libro -> libro.getDeath_year()>=fecha)
                .collect(Collectors.toList());
        if (librosFiltrados.isEmpty()) {
            System.out.println("❌ No hay libros de autores que vivieron en el año " + fecha);
        } else {
            System.out.println("\n📚 Libros de autores que vivieron en " + fecha + ":");
            librosFiltrados.forEach(libro -> System.out.println(
                    "- " + libro.getTitle()+ "\n" + libro.getAuthorName() +"\n" +
                            "Vivo: " + libro.getBirth_year() + "–" +
                            (libro.getDeath_year() == null ? "Presente" : libro.getDeath_year()+ "\n")
            ));
        }
    }

    //METODO PARA PODER FILTRAR LOS LIBROS POR IDIOMA
    private void mostrarLibrosPorIdioma(String idioma) {
        List<Libro> librosFiltrados = libroRepository.findByLanguageContaining(idioma.toLowerCase());

        if (librosFiltrados.isEmpty()) {
            System.out.println("❌ No hay libros en idioma: " + idioma);
        } else {
            System.out.println("\n📚 Libros en idioma " + idioma + ":");
            librosFiltrados.forEach(System.out::println);
        }
    }
}

