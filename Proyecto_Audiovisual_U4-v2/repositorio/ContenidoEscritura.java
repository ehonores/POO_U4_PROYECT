package repositorio;

import modelo.ContenidoAudiovisual;

public interface ContenidoEscritura {
    void agregar(ContenidoAudiovisual contenido);
    boolean eliminarPorId(String id);
}
