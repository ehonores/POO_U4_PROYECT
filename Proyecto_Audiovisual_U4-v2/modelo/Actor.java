package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Actor {
    private final String id;
    private String nombre;
    private String nacionalidad;
    private int anioNacimiento;

    public Actor(String id, String nombre, String nacionalidad, int anioNacimiento) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del actor no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del actor no puede estar vacío.");
        }
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.nacionalidad = nacionalidad == null || nacionalidad.isBlank() ? "Desconocida" : nacionalidad.trim();
        this.anioNacimiento = anioNacimiento;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del actor no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad == null || nacionalidad.isBlank() ? "Desconocida" : nacionalidad.trim();
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public String toCsvLine() {
        return String.join(",",
                escapeCsv(id),
                escapeCsv(nombre),
                escapeCsv(nacionalidad),
                String.valueOf(anioNacimiento));
    }

    public static Actor fromCsv(String line) {
        String[] parts = splitCsv(line);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Línea CSV de Actor inválida: " + line);
        }
        return new Actor(parts[0], parts[1], parts[2], Integer.parseInt(parts[3].trim()));
    }

    public static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static String[] splitCsv(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    current.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor actor)) return false;
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Actor[%s: %s (%s, %d)]", id, nombre, nacionalidad, anioNacimiento);
    }
}
