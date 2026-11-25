package repositorio;

import modelo.Actor;
import modelo.ContenidoAudiovisual;
import modelo.Investigador;
import modelo.Temporada;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class RepositorioAudiovisualMemoria implements ContenidoLectura, ContenidoEscritura {
    private final List<ContenidoAudiovisual> contenidos = new ArrayList<>();
    private final List<Actor> actores = new ArrayList<>();
    private final List<Temporada> temporadas = new ArrayList<>();
    private final List<Investigador> investigadores = new ArrayList<>();

    @Override
    public void agregar(ContenidoAudiovisual contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException("El contenido no puede ser nulo.");
        }
        contenidos.removeIf(item -> item.getId().equals(contenido.getId()));
        contenidos.add(contenido);
    }

    @Override
    public boolean eliminarPorId(String id) {
        if (id == null || id.isBlank()) return false;
        return contenidos.removeIf(item -> item.getId().equals(id.trim()));
    }

    @Override
    public List<ContenidoAudiovisual> obtenerTodos() {
        return Collections.unmodifiableList(contenidos);
    }

    @Override
    public Optional<ContenidoAudiovisual> buscarPorId(String id) {
        if (id == null) return Optional.empty();
        return contenidos.stream()
                .filter(item -> item.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    @Override
    public List<ContenidoAudiovisual> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) return List.of();
        String query = titulo.trim().toLowerCase(Locale.ROOT);
        return contenidos.stream()
                .filter(item -> item.getTitulo().toLowerCase(Locale.ROOT).contains(query))
                .toList();
    }

    public void agregarActor(Actor actor) {
        if (actor == null) throw new IllegalArgumentException("El actor no puede ser nulo.");
        actores.removeIf(a -> a.getId().equals(actor.getId()));
        actores.add(actor);
    }

    public List<Actor> obtenerActores() {
        return Collections.unmodifiableList(actores);
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada == null) throw new IllegalArgumentException("La temporada no puede ser nula.");
        temporadas.removeIf(t -> t.getId().equals(temporada.getId()));
        temporadas.add(temporada);
    }

    public List<Temporada> obtenerTemporadas() {
        return Collections.unmodifiableList(temporadas);
    }

    public void agregarInvestigador(Investigador investigador) {
        if (investigador == null) throw new IllegalArgumentException("El investigador no puede ser nulo.");
        investigadores.removeIf(i -> i.getId().equals(investigador.getId()));
        investigadores.add(investigador);
    }

    public List<Investigador> obtenerInvestigadores() {
        return Collections.unmodifiableList(investigadores);
    }

    public void cargarEstado(
            List<ContenidoAudiovisual> nuevosContenidos,
            List<Actor> nuevosActores,
            List<Temporada> nuevasTemporadas,
            List<Investigador> nuevosInvestigadores) {
        contenidos.clear();
        actores.clear();
        temporadas.clear();
        investigadores.clear();

        if (nuevosContenidos != null) contenidos.addAll(nuevosContenidos);
        if (nuevosActores != null) actores.addAll(nuevosActores);
        if (nuevasTemporadas != null) temporadas.addAll(nuevasTemporadas);
        if (nuevosInvestigadores != null) investigadores.addAll(nuevosInvestigadores);
    }
}
