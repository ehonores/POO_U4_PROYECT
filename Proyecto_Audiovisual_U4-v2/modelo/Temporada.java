package modelo;

import java.util.Objects;

public class Temporada {
    private final String id;
    private final String serieId;
    private int numeroTemporada;
    private int cantidadEpisodios;
    private int anioLanzamiento;

    public Temporada(String id, String serieId, int numeroTemporada, int cantidadEpisodios, int anioLanzamiento) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la temporada no puede estar vacío.");
        }
        if (serieId == null || serieId.isBlank()) {
            throw new IllegalArgumentException("El ID de la serie no puede estar vacío.");
        }
        if (numeroTemporada <= 0) {
            throw new IllegalArgumentException("El número de temporada debe ser mayor que cero.");
        }
        if (cantidadEpisodios <= 0) {
            throw new IllegalArgumentException("La cantidad de episodios debe ser mayor que cero.");
        }
        this.id = id.trim();
        this.serieId = serieId.trim();
        this.numeroTemporada = numeroTemporada;
        this.cantidadEpisodios = cantidadEpisodios;
        this.anioLanzamiento = anioLanzamiento;
    }

    public String getId() {
        return id;
    }

    public String getSerieId() {
        return serieId;
    }

    public int getNumeroTemporada() {
        return numeroTemporada;
    }

    public void setNumeroTemporada(int numeroTemporada) {
        if (numeroTemporada <= 0) {
            throw new IllegalArgumentException("El número de temporada debe ser mayor que cero.");
        }
        this.numeroTemporada = numeroTemporada;
    }

    public int getCantidadEpisodios() {
        return cantidadEpisodios;
    }

    public void setCantidadEpisodios(int cantidadEpisodios) {
        if (cantidadEpisodios <= 0) {
            throw new IllegalArgumentException("La cantidad de episodios debe ser mayor que cero.");
        }
        this.cantidadEpisodios = cantidadEpisodios;
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(int anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public String toCsvLine() {
        return String.join(",",
                Actor.escapeCsv(id),
                Actor.escapeCsv(serieId),
                String.valueOf(numeroTemporada),
                String.valueOf(cantidadEpisodios),
                String.valueOf(anioLanzamiento));
    }

    public static Temporada fromCsv(String line) {
        String[] parts = Actor.splitCsv(line);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Línea CSV de Temporada inválida: " + line);
        }
        return new Temporada(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                Integer.parseInt(parts[4].trim())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temporada temporada)) return false;
        return Objects.equals(id, temporada.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Temporada[%s - Serie %s: T%d (%d ep., %d)]",
                id, serieId, numeroTemporada, cantidadEpisodios, anioLanzamiento);
    }
}
