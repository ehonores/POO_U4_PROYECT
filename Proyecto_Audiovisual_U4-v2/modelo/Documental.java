package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Documental extends ContenidoAudiovisual {
    private String temaPrincipal;
    private final List<String> investigadorIds;

    public Documental(String id, String titulo, int duracionMinutos, int anioEstreno, String genero, String temaPrincipal) {
        super(id, titulo, duracionMinutos, anioEstreno, genero);
        this.temaPrincipal = temaPrincipal == null || temaPrincipal.isBlank() ? "General" : temaPrincipal.trim();
        this.investigadorIds = new ArrayList<>();
    }

    public String getTemaPrincipal() {
        return temaPrincipal;
    }

    public void setTemaPrincipal(String temaPrincipal) {
        this.temaPrincipal = temaPrincipal == null || temaPrincipal.isBlank() ? "General" : temaPrincipal.trim();
    }

    public void agregarInvestigadorId(String investigadorId) {
        if (investigadorId != null && !investigadorId.isBlank() && !investigadorIds.contains(investigadorId.trim())) {
            investigadorIds.add(investigadorId.trim());
        }
    }

    public List<String> getInvestigadorIds() {
        return Collections.unmodifiableList(investigadorIds);
    }

    @Override
    public String getTipoContenido() {
        return "DOCUMENTAL";
    }

    @Override
    public String getResumen() {
        return super.getResumen() + String.format(" | Tema: %s | Investigadores: %s",
                temaPrincipal, String.join(";", investigadorIds));
    }

    @Override
    public String toCsvLine() {
        return String.join(",",
                Actor.escapeCsv(getId()),
                Actor.escapeCsv(getTitulo()),
                String.valueOf(getDuracionMinutos()),
                String.valueOf(getAnioEstreno()),
                Actor.escapeCsv(getGenero()),
                Actor.escapeCsv(temaPrincipal),
                Actor.escapeCsv(String.join(";", investigadorIds)));
    }

    public static Documental fromCsv(String line) {
        String[] parts = Actor.splitCsv(line);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Línea CSV de Documental inválida: " + line);
        }
        Documental doc = new Documental(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                parts[4],
                parts[5]
        );
        if (parts.length >= 7 && !parts[6].isBlank()) {
            for (String invId : parts[6].split(";")) {
                doc.agregarInvestigadorId(invId);
            }
        }
        return doc;
    }
}
