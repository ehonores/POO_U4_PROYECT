package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerieTV extends ContenidoAudiovisual {
    private String creador;
    private final List<Temporada> temporadas;

    public SerieTV(String id, String titulo, int duracionPromedioEpisodio, int anioEstreno, String genero, String creador) {
        super(id, titulo, duracionPromedioEpisodio, anioEstreno, genero);
        this.creador = creador == null || creador.isBlank() ? "Desconocido" : creador.trim();
        this.temporadas = new ArrayList<>();
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador == null || creador.isBlank() ? "Desconocido" : creador.trim();
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada != null && !temporadas.contains(temporada)) {
            temporadas.add(temporada);
        }
    }

    public List<Temporada> getTemporadas() {
        return Collections.unmodifiableList(temporadas);
    }

    public int getTotalEpisodios() {
        int total = 0;
        for (Temporada t : temporadas) {
            total += t.getCantidadEpisodios();
        }
        return total;
    }

    @Override
    public String getTipoContenido() {
        return "SERIE_TV";
    }

    @Override
    public String getResumen() {
        return super.getResumen() + String.format(" | Creador: %s | Temporadas: %d | Episodios: %d",
                creador, temporadas.size(), getTotalEpisodios());
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                Actor.escapeCsv(getId()),
                Actor.escapeCsv(getTitulo()),
                String.valueOf(getDuracionMinutos()),
                String.valueOf(getAnioEstreno()),
                Actor.escapeCsv(getGenero()),
                Actor.escapeCsv(creador));
    }

    public static SerieTV fromCsv(String line) {
        String[] parts = Actor.splitCsv(line);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Línea CSV de SerieTV inválida: " + line);
        }
        return new SerieTV(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                parts[4],
                parts[5]
        );
    }
}
