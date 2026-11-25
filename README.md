# Sistema de Gestión de Contenido Audiovisual

Aplicación desarrollada en **Java** bajo el paradigma de Programación Orientada a Objetos (POO), refactorizada aplicando principios de **Código Limpio**, **SOLID**, arquitectura **MVC** en consola, persistencia en archivos **CSV** y pruebas unitarias con **JUnit**.

---

##  Características Principales

* **Gestión integral de entidades:** Registro, consulta y administración de películas, series de TV, documentales, actores, temporadas e investigadores.
* **Persistencia en CSV:** Lectura y escritura automática de los datos en archivos `.csv` (codificación UTF-8) dentro de la carpeta `datos/`, conservando la información entre sesiones.
* **Arquitectura MVC:** Separación clara entre la lógica de datos (`modelo` y `repositorio`), la interfaz de usuario en consola (`ConsolaVista`) y el flujo de control (`AudiovisualController`).
* **Calidad de código y SOLID:** Código modular con responsabilidades únicas, interfaces segregadas (`ContenidoLectura` y `ContenidoEscritura`) y manejo seguro de excepciones.
* **Pruebas automatizadas:** Validación del comportamiento del sistema y de la persistencia mediante pruebas unitarias en JUnit.

---

##  Estructura del Proyecto

La estructura del proyecto está organizada en cinco carpetas principales para mantener un diseño limpio y fácil de navegar:

```text
Proyecto-Audiovisual/
├── datos/               # Archivos CSV de almacenamiento (peliculas, series, documentales, etc.)
├── modelo/              # Clases del dominio (ContenidoAudiovisual, Pelicula, SerieTV, Documental, Actor, Temporada, Investigador)
├── repositorio/         # Interfaces de acceso, repositorio en memoria y clase PersistenciaCsv
├── mvc/                 # Punto de entrada (Main), vista en consola (ConsolaVista) y controlador (AudiovisualController)
└── test/                # Pruebas unitarias desarrolladas con JUnit
