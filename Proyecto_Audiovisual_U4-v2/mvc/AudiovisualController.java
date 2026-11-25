package mvc;

import modelo.Actor;
import modelo.ContenidoAudiovisual;
import modelo.Documental;
import modelo.Investigador;
import modelo.Pelicula;
import modelo.SerieTV;
import modelo.Temporada;
import repositorio.ContenidoEscritura;
import repositorio.ContenidoLectura;
import repositorio.PersistenciaCsv;
import repositorio.RepositorioAudiovisualMemoria;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AudiovisualController {
    private final ContenidoLectura lecturaRepo;
    private final ContenidoEscritura escrituraRepo;
    private final RepositorioAudiovisualMemoria repositorioCompleto;
    private final PersistenciaCsv persistenciaCsv;
    private final ConsolaVista vista;

    public AudiovisualController(
            RepositorioAudiovisualMemoria repositorio,
            PersistenciaCsv persistenciaCsv,
            ConsolaVista vista) {
        this.repositorioCompleto = repositorio;
        this.lecturaRepo = repositorio;
        this.escrituraRepo = repositorio;
        this.persistenciaCsv = persistenciaCsv;
        this.vista = vista;
    }

    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            vista.mostrarMenu();
            int opcion = vista.leerEntero("");
            continuar = procesarOpcion(opcion);
        }
    }

    public boolean procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> vista.mostrarContenidos(lecturaRepo.obtenerTodos());
                case 2 -> buscarPorTitulo();
                case 3 -> registrarPelicula();
                case 4 -> registrarSerie();
                case 5 -> registrarDocumental();
                case 6 -> registrarTemporada();
                case 7 -> registrarActor();
                case 8 -> registrarInvestigador();
                case 9 -> guardarEnCsv();
                case 10 -> cargarDesdeCsv();
                case 11 -> vista.mostrarDetalleEntidades(
                        repositorioCompleto.obtenerActores(),
                        repositorioCompleto.obtenerTemporadas(),
                        repositorioCompleto.obtenerInvestigadores());
                case 0 -> {
                    vista.mostrarMensaje("Saliendo del sistema...");
                    return false;
                }
                default -> vista.mostrarMensaje("Opción no válida.");
            }
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
        return true;
    }

    private void buscarPorTitulo() {
        String titulo = vista.leerLinea("Ingrese título a buscar: ");
        List<ContenidoAudiovisual> resultados = lecturaRepo.buscarPorTitulo(titulo);
        vista.mostrarContenidos(resultados);
    }

    private void registrarPelicula() {
        String id = vista.leerLinea("ID película: ");
        String titulo = vista.leerLinea("Título: ");
        int duracion = vista.leerEntero("Duración (minutos): ");
        int anio = vista.leerEntero("Año de estreno: ");
        String genero = vista.leerLinea("Género: ");
        String director = vista.leerLinea("Director: ");
        Pelicula pelicula = new Pelicula(id, titulo, duracion, anio, genero, director);
        escrituraRepo.agregar(pelicula);
        vista.mostrarMensaje("Película registrada exitosamente.");
    }

    private void registrarSerie() {
        String id = vista.leerLinea("ID serie: ");
        String titulo = vista.leerLinea("Título: ");
        int duracion = vista.leerEntero("Duración promedio episodio (min): ");
        int anio = vista.leerEntero("Año de estreno: ");
        String genero = vista.leerLinea("Género: ");
        String creador = vista.leerLinea("Creador: ");
        SerieTV serie = new SerieTV(id, titulo, duracion, anio, genero, creador);
        escrituraRepo.agregar(serie);
        vista.mostrarMensaje("Serie registrada exitosamente.");
    }

    private void registrarDocumental() {
        String id = vista.leerLinea("ID documental: ");
        String titulo = vista.leerLinea("Título: ");
        int duracion = vista.leerEntero("Duración (minutos): ");
        int anio = vista.leerEntero("Año de estreno: ");
        String genero = vista.leerLinea("Género: ");
        String tema = vista.leerLinea("Tema principal: ");
        Documental documental = new Documental(id, titulo, duracion, anio, genero, tema);
        escrituraRepo.agregar(documental);
        vista.mostrarMensaje("Documental registrado exitosamente.");
    }

    private void registrarTemporada() {
        String id = vista.leerLinea("ID temporada: ");
        String serieId = vista.leerLinea("ID de la serie: ");
        int numero = vista.leerEntero("Número de temporada: ");
        int episodios = vista.leerEntero("Cantidad de episodios: ");
        int anio = vista.leerEntero("Año de lanzamiento: ");
        Temporada temporada = new Temporada(id, serieId, numero, episodios, anio);
        repositorioCompleto.agregarTemporada(temporada);

        Optional<ContenidoAudiovisual> optSerie = lecturaRepo.buscarPorId(serieId);
        if (optSerie.isPresent() && optSerie.get() instanceof SerieTV serie) {
            serie.agregarTemporada(temporada);
        }
        vista.mostrarMensaje("Temporada registrada exitosamente.");
    }

    private void registrarActor() {
        String id = vista.leerLinea("ID actor: ");
        String nombre = vista.leerLinea("Nombre: ");
        String nacionalidad = vista.leerLinea("Nacionalidad: ");
        int anioNacimiento = vista.leerEntero("Año de nacimiento: ");
        repositorioCompleto.agregarActor(new Actor(id, nombre, nacionalidad, anioNacimiento));
        vista.mostrarMensaje("Actor registrado exitosamente.");
    }

    private void registrarInvestigador() {
        String id = vista.leerLinea("ID investigador: ");
        String nombre = vista.leerLinea("Nombre: ");
        String especialidad = vista.leerLinea("Especialidad: ");
        String institucion = vista.leerLinea("Institución: ");
        repositorioCompleto.agregarInvestigador(new Investigador(id, nombre, especialidad, institucion));
        vista.mostrarMensaje("Investigador registrado exitosamente.");
    }

    public void guardarEnCsv() {
        try {
            persistenciaCsv.guardarTodo(
                    lecturaRepo.obtenerTodos(),
                    repositorioCompleto.obtenerActores(),
                    repositorioCompleto.obtenerTemporadas(),
                    repositorioCompleto.obtenerInvestigadores()
            );
            vista.mostrarMensaje("Datos guardados en CSV correctamente.");
        } catch (IOException e) {
            vista.mostrarError("No se pudieron guardar los archivos CSV: " + e.getMessage());
        }
    }

    public void cargarDesdeCsv() {
        try {
            List<Actor> actores = persistenciaCsv.cargarActores();
            List<Temporada> temporadas = persistenciaCsv.cargarTemporadas();
            List<Investigador> investigadores = persistenciaCsv.cargarInvestigadores();
            List<ContenidoAudiovisual> contenidos = persistenciaCsv.cargarContenidos(temporadas);
            repositorioCompleto.cargarEstado(contenidos, actores, temporadas, investigadores);
            vista.mostrarMensaje("Datos cargados desde CSV correctamente.");
        } catch (IOException | IllegalArgumentException e) {
            vista.mostrarError("No se pudieron cargar los archivos CSV: " + e.getMessage());
        }
    }
}
