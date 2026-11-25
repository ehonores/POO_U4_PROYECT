package mvc;

import modelo.Actor;
import modelo.ContenidoAudiovisual;
import modelo.Investigador;
import modelo.Temporada;
import java.util.List;
import java.util.Scanner;

public class ConsolaVista {
    private final Scanner scanner;

    public ConsolaVista(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE CONTENIDO AUDIOVISUAL (MVC + SOLID) ===");
        System.out.println("1. Listar todos los contenidos");
        System.out.println("2. Buscar contenido por título");
        System.out.println("3. Agregar película");
        System.out.println("4. Agregar serie de TV");
        System.out.println("5. Agregar documental");
        System.out.println("6. Agregar temporada a serie");
        System.out.println("7. Agregar actor");
        System.out.println("8. Agregar investigador");
        System.out.println("9. Guardar datos en CSV");
        System.out.println("10. Cargar datos desde CSV");
        System.out.println("11. Listar actores, temporadas e investigadores");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public String leerLinea(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    public void mostrarContenidos(List<ContenidoAudiovisual> contenidos) {
        if (contenidos.isEmpty()) {
            System.out.println("No hay contenidos registrados.");
            return;
        }
        System.out.println("\n--- Contenidos Registrados ---");
        for (ContenidoAudiovisual c : contenidos) {
            System.out.println(c.getResumen());
        }
    }

    public void mostrarDetalleEntidades(List<Actor> actores, List<Temporada> temporadas, List<Investigador> investigadores) {
        System.out.println("\n--- Actores ---");
        actores.forEach(a -> System.out.println(a.toString()));
        System.out.println("\n--- Temporadas ---");
        temporadas.forEach(t -> System.out.println(t.toString()));
        System.out.println("\n--- Investigadores ---");
        investigadores.forEach(i -> System.out.println(i.toString()));
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("Error: " + error);
    }
}
