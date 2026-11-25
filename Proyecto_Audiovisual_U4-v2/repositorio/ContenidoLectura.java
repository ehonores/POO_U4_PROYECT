package repositorio;

import modelo.ContenidoAudiovisual;
import java.util.List;
import java.util.Optional;

public interface ContenidoLectura {
    List<ContenidoAudiovisual> obtenerTodos();
    Optional<ContenidoAudiovisual> buscarPorId(String id);
    List<ContenidoAudiovisual> buscarPorTitulo(String titulo);
}
