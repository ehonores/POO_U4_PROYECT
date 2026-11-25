package repositorio;

import modelo.Actor;
import modelo.ContenidoAudiovisual;
import modelo.Documental;
import modelo.Investigador;
import modelo.Pelicula;
import modelo.SerieTV;
import modelo.Temporada;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCsv {
    private static final String HEADER_PELICULAS = "id,titulo,duracionMinutos,anioEstreno,genero,director,actorIds";
    private static final String HEADER_SERIES = "id,titulo,duracionMinutos,anioEstreno,genero,creador";
    private static final String HEADER_DOCUMENTALES = "id,titulo,duracionMinutos,anioEstreno,genero,temaPrincipal,investigadorIds";
    private static final String HEADER_ACTORES = "id,nombre,nacionalidad,anioNacimiento";
    private static final String HEADER_TEMPORADAS = "id,serieId,numeroTemporada,cantidadEpisodios,anioLanzamiento";
    private static final String HEADER_INVESTIGADORES = "id,nombre,especialidad,institucion";

    private final Path peliculasPath;
    private final Path seriesPath;
    private final Path documentalesPath;
    private final Path actoresPath;
    private final Path temporadasPath;
    private final Path investigadoresPath;

    public PersistenciaCsv(Path directorioDatos) {
        if (directorioDatos == null) {
            throw new IllegalArgumentException("El directorio de datos no puede ser nulo.");
        }
        this.peliculasPath = directorioDatos.resolve("peliculas.csv");
        this.seriesPath = directorioDatos.resolve("series.csv");
        this.documentalesPath = directorioDatos.resolve("documentales.csv");
        this.actoresPath = directorioDatos.resolve("actores.csv");
        this.temporadasPath = directorioDatos.resolve("temporadas.csv");
        this.investigadoresPath = directorioDatos.resolve("investigadores.csv");
    }

    public void guardarTodo(
            List<ContenidoAudiovisual> contenidos,
            List<Actor> actores,
            List<Temporada> temporadas,
            List<Investigador> investigadores) throws IOException {

        List<Pelicula> peliculas = new ArrayList<>();
        List<SerieTV> series = new ArrayList<>();
        List<Documental> documentales = new ArrayList<>();

        for (ContenidoAudiovisual c : contenidos) {
            if (c instanceof Pelicula p) {
                peliculas.add(p);
            } else if (c instanceof SerieTV s) {
                series.add(s);
            } else if (c instanceof Documental d) {
                documentales.add(d);
            }
        }

        escribirArchivo(peliculasPath, HEADER_PELICULAS, peliculas.stream().map(Pelicula::toCsvLine).toList());
        escribirArchivo(seriesPath, HEADER_SERIES, series.stream().map(SerieTV::toCsvLine).toList());
        escribirArchivo(documentalesPath, HEADER_DOCUMENTALES, documentales.stream().map(Documental::toCsvLine).toList());
        escribirArchivo(actoresPath, HEADER_ACTORES, actores.stream().map(Actor::toCsvLine).toList());
        escribirArchivo(temporadasPath, HEADER_TEMPORADAS, temporadas.stream().map(Temporada::toCsvLine).toList());
        escribirArchivo(investigadoresPath, HEADER_INVESTIGADORES, investigadores.stream().map(Investigador::toCsvLine).toList());
    }

    public List<Actor> cargarActores() throws IOException {
        List<Actor> result = new ArrayList<>();
        for (String line : leerLineasDatos(actoresPath)) {
            result.add(Actor.fromCsv(line));
        }
        return result;
    }

    public List<Investigador> cargarInvestigadores() throws IOException {
        List<Investigador> result = new ArrayList<>();
        for (String line : leerLineasDatos(investigadoresPath)) {
            result.add(Investigador.fromCsv(line));
        }
        return result;
    }

    public List<Temporada> cargarTemporadas() throws IOException {
        List<Temporada> result = new ArrayList<>();
        for (String line : leerLineasDatos(temporadasPath)) {
            result.add(Temporada.fromCsv(line));
        }
        return result;
    }

    public List<ContenidoAudiovisual> cargarContenidos(List<Temporada> temporadas) throws IOException {
        List<ContenidoAudiovisual> contenidos = new ArrayList<>();

        for (String line : leerLineasDatos(peliculasPath)) {
            contenidos.add(Pelicula.fromCsv(line));
        }

        List<SerieTV> series = new ArrayList<>();
        for (String line : leerLineasDatos(seriesPath)) {
            SerieTV serie = SerieTV.fromCsv(line);
            for (Temporada t : temporadas) {
                if (t.getSerieId().equals(serie.getId())) {
                    serie.agregarTemporada(t);
                }
            }
            series.add(serie);
        }
        contenidos.addAll(series);

        for (String line : leerLineasDatos(documentalesPath)) {
            contenidos.add(Documental.fromCsv(line));
        }

        return contenidos;
    }

    private void escribirArchivo(Path path, String header, List<String> rows) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        List<String> lines = new ArrayList<>();
        lines.add(header);
        lines.addAll(rows);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private List<String> leerLineasDatos(Path path) throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> dataLines = new ArrayList<>();
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i).trim();
            if (line.isEmpty()) continue;
            if (i == 0 && esEncabezado(line)) continue;
            dataLines.add(line);
        }
        return dataLines;
    }

    private boolean esEncabezado(String line) {
        return line.startsWith("id,titulo,") || line.startsWith("id,nombre,") || line.startsWith("id,serieId,");
    }
}
