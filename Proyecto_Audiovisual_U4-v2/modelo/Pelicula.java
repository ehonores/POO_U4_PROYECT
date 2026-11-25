package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pelicula extends ContenidoAudiovisual {
    private String director;
    private final List<String> actorIds;

    public Pelicula(String id, String titulo, int duracionMinutos, int anioEstreno, String genero, String director) {
        super(id, titulo, duracionMinutos, anioEstreno, genero);
        this.director = director == null || director.isBlank() ? "Desconocido" : director.trim();
        this.actorIds = new ArrayList<>();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null || director.isBlank() ? "Desconocido" : director.trim();
    }

    public void agregarActorId(String actorId) {
        if (actorId != null && !actorId.isBlank() && !actorIds.contains(actorId.trim())) {
            actorIds.add(actorId.trim());
        }
    }

    public List<String> getActorIds() {
        return Collections.unmodifiableList(actorIds);
    }

    @Override
    public String getTipoContenido() {
        return "PELICULA";
    }

    @Override
    public String getResumen() {
        return super.getResumen() + String.format(" | Director: %s | Actores: %s",
                director, String.join(";", actorIds));
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                Actor.escapeCsv(getId()),
                Actor.escapeCsv(getTitulo()),
                String.valueOf(getDuracionMinutos()),
                String.valueOf(getAnioEstreno()),
                Actor.escapeCsv(getGenero()),
                Actor.escapeCsv(director),
                Actor.escapeCsv(String.join(";", actorIds)));
    }

    public static Pelicula fromCsv(String line) {
        String[] parts = Actor.splitCsv(line);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Línea CSV de Película inválida: " + line);
        }
        Pelicula pelicula = new Pelicula(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                parts[4],
                parts[5]
        );
        if (parts.length >= 7 && !parts[6].isBlank()) {
            for (String actorId : parts[6].split(";")) {
                pelicula.agregarActorId(actorId);
            }
        }
        return pelicula;
    }
}
