package modelo;

import java.util.Objects;

public class Investigador {
    private final String id;
    private String nombre;
    private String especialidad;
    private String institucion;

    public Investigador(String id, String nombre, String especialidad, String institucion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del investigador no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del investigador no puede estar vacío.");
        }
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.especialidad = especialidad == null || especialidad.isBlank() ? "General" : especialidad.trim();
        this.institucion = institucion == null || institucion.isBlank() ? "Independiente" : institucion.trim();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del investigador no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad == null || especialidad.isBlank() ? "General" : especialidad.trim();
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion == null || institucion.isBlank() ? "Independiente" : institucion.trim();
    }

    public String toCsvLine() {
        return String.join(",",
                Actor.escapeCsv(id),
                Actor.escapeCsv(nombre),
                Actor.escapeCsv(especialidad),
                Actor.escapeCsv(institucion));
    }

    public static Investigador fromCsv(String line) {
        String[] parts = Actor.splitCsv(line);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Línea CSV de Investigador inválida: " + line);
        }
        return new Investigador(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investigador that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Investigador[%s: %s - %s (%s)]", id, nombre, especialidad, institucion);
    }
}
