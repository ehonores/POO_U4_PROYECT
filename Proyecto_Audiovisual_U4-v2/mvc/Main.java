package mvc;


import repositorio.PersistenciaCsv;
import repositorio.RepositorioAudiovisualMemoria;
import com.ups.audiovisual.view.ConsolaVista;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path rutaDatos = Path.of("datos");
        RepositorioAudiovisualMemoria repositorio = new RepositorioAudiovisualMemoria();
        PersistenciaCsv persistencia = new PersistenciaCsv(rutaDatos);
        ConsolaVista vista = new ConsolaVista(new Scanner(System.in));
        AudiovisualController controlador = new AudiovisualController(repositorio, persistencia, vista);
        controlador.ejecutar();
    }
}
