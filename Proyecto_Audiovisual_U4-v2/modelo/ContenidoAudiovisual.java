package modelo;

import java.util.Objects;

public abstract class ContenidoAudiovisual {
    private final String id;
    private String titulo;
    private int duracionMinutos;
    private int anioEstreno;
    private String genero;

    protected ContenidoAudiovisual(String id, String titulo, int duracionMinutos, int anioEstreno, String genero) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        if (anioEstreno < 1880 || anioEstreno > 2100) {
            throw new IllegalArgumentException("El año de estreno es inválido.");
        }
        this.id = id.trim();
        this.titulo = titulo.trim();
        this.duracionMinutos = duracionMinutos;
        this.anioEstreno = anioEstreno;
        this.genero = genero == null || genero.isBlank() ? "General" : genero.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.duracionMinutos = duracionMinutos;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(int anioEstreno) {
        if (anioEstreno < 1880 || anioEstreno > 2100) {
            throw new IllegalArgumentException("El año de estreno es inválido.");
        }
        this.anioEstreno = anioEstreno;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero == null || genero.isBlank() ? "General" : genero.trim();
    }

    public abstract String getTipoContenido();

    public abstract String toCsvLine();

    public String getResumen() {
        return String.format("[%s] %s (%s, %d) - %d min - ID: %s",
                getTipoContenido(), titulo, genero, anioEstreno, duracionMinutos, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenidoAudiovisual that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
