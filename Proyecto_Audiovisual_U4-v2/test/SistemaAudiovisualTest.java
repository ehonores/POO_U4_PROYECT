package test;

import modelo.Actor;
import modelo.ContenidoAudiovisual;
import modelo.Documental;
import modelo.Investigador;
import modelo.Pelicula;
import modelo.SerieTV;
import modelo.Temporada;
import repositorio.PersistenciaCsv;
import repositorio.RepositorioAudiovisualMemoria;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class SistemaAudiovisualTest {

    @TempDir
    Path tempDir;

    private RepositorioAudiovisualMemoria repositorio;
    private PersistenciaCsv persistencia;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioAudiovisualMemoria();
        persistencia = new PersistenciaCsv(tempDir);
    }

    @Test
    void deberiaRegistrarYBuscarContenidosPolimorficamente() {
        Pelicula pelicula = new Pelicula("P01", "Interestelar", 169, 2014, "Ciencia Ficción", "Christopher Nolan");
        pelicula.agregarActorId("A01");

        SerieTV serie = new SerieTV("S01", "Dark", 50, 2017, "Misterio", "Baran bo Odar");
        Temporada t1 = new Temporada("T01", "S01", 1, 10, 2017);
        serie.agregarTemporada(t1);

        Documental doc = new Documental("D01", "Nuestro Planeta", 52, 2019, "Naturaleza", "Biodiversidad");
        doc.agregarInvestigadorId("I01");

        repositorio.agregar(pelicula);
        repositorio.agregar(serie);
        repositorio.agregar(doc);

        assertEquals(3, repositorio.obtenerTodos().size());
        assertEquals(10, serie.getTotalEpisodios());
        assertEquals(1, repositorio.buscarPorTitulo("Dark").size());
        assertTrue(repositorio.buscarPorId("P01").isPresent());
    }

    @Test
    void deberiaGuardarYCargarTodosLosDatosEnCsv() throws IOException {
        Pelicula pelicula = new Pelicula("P01", "Origen", 148, 2010, "Acción", "Christopher Nolan");
        pelicula.agregarActorId("A01");

        SerieTV serie = new SerieTV("S01", "Breaking Bad", 47, 2008, "Drama", "Vince Gilligan");
        Temporada temporada = new Temporada("T01", "S01", 1, 7, 2008);
        serie.agregarTemporada(temporada);

        Documental documental = new Documental("D01", "Cosmos", 45, 2014, "Ciencia", "Astrofísica");
        documental.agregarInvestigadorId("I01");

        Actor actor = new Actor("A01", "Leonardo DiCaprio", "Estadounidense", 1974);
        Investigador investigador = new Investigador("I01", "Neil deGrasse Tyson", "Astrofísica", "AMNH");

        repositorio.agregar(pelicula);
        repositorio.agregar(serie);
        repositorio.agregar(documental);
        repositorio.agregarActor(actor);
        repositorio.agregarTemporada(temporada);
        repositorio.agregarInvestigador(investigador);

        persistencia.guardarTodo(
                repositorio.obtenerTodos(),
                repositorio.obtenerActores(),
                repositorio.obtenerTemporadas(),
                repositorio.obtenerInvestigadores()
        );

        RepositorioAudiovisualMemoria cargado = new RepositorioAudiovisualMemoria();
        List<Actor> actoresCargados = persistencia.cargarActores();
        List<Temporada> temporadasCargadas = persistencia.cargarTemporadas();
        List<Investigador> investigadoresCargados = persistencia.cargarInvestigadores();
        List<ContenidoAudiovisual> contenidosCargados = persistencia.cargarContenidos(temporadasCargadas);
        cargado.cargarEstado(contenidosCargados, actoresCargados, temporadasCargadas, investigadoresCargados);

        assertEquals(3, cargado.obtenerTodos().size());
        assertEquals(1, cargado.obtenerActores().size());
        assertEquals(1, cargado.obtenerTemporadas().size());
        assertEquals(1, cargado.obtenerInvestigadores().size());

        ContenidoAudiovisual serieRecuperada = cargado.buscarPorId("S01").orElseThrow();
        assertInstanceOf(SerieTV.class, serieRecuperada);
        assertEquals(7, ((SerieTV) serieRecuperada).getTotalEpisodios());
    }

    @Test
    void deberiaRechazarDatosInvalidosEnEntidades() {
        assertThrows(IllegalArgumentException.class, () ->
                new Pelicula("", "Título", 120, 2020, "Drama", "Director"));
        assertThrows(IllegalArgumentException.class, () ->
                new Pelicula("P02", "Título", -10, 2020, "Drama", "Director"));
        assertThrows(IllegalArgumentException.class, () ->
                new Temporada("T02", "S01", 0, 5, 2020));
        assertThrows(IllegalArgumentException.class, () ->
                new Actor("A02", "", "Ecuador", 1995));
        assertThrows(IllegalArgumentException.class, () ->
                new Investigador("I02", "", "Física", "UPS"));
    }

    @Test
    void deberiaManejarEliminacionYBusquedaVacia() {
        Pelicula p = new Pelicula("P10", "Matrix", 136, 1999, "Ciencia Ficción", "Wachowski");
        repositorio.agregar(p);

        assertTrue(repositorio.eliminarPorId("P10"));
        assertFalse(repositorio.eliminarPorId("P10"));
        assertTrue(repositorio.buscarPorTitulo("Inexistente").isEmpty());
        assertTrue(repositorio.buscarPorId("NO_EXISTE").isEmpty());
    }
}
