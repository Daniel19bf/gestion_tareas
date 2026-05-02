# Arquitectura Hexagonal - Gestor de Tareas (Java)

Este proyecto implementa una aplicación de gestión de tareas con interfaz gráfica (Java Swing), diseñada **estrictamente** bajo los principios de la Arquitectura Hexagonal (Ports & Adapters).

No se ha utilizado ningún framework (como Spring o Hibernate), basándose exclusivamente en Programación Orientada a Objetos pura y las APIs de Java estándar.

## 🧱 Explicación de la Arquitectura

La Arquitectura Hexagonal aísla la lógica central de la aplicación del exterior, permitiendo que la aplicación sea dirigida de igual manera por usuarios, programas o tests automáticos, y que sea desarrollada y testeada en aislamiento de sus bases de datos o dispositivos.

Para lograr esto, la arquitectura se divide en tres partes fundamentales:

### 1. Dominio (`domain`)
Es el corazón del software. Contiene la lógica de negocio pura y las entidades.
- **Entidad `Tarea`**: Encapsula sus atributos (`id`, `titulo`, `estado`) y sus propias reglas de validación (por ejemplo, el título no puede estar vacío y solo la entidad puede modificar su estado de "pendiente" a "completada").
- *Aislamiento:* Esta capa no importa NADA fuera de ella. Es POO pura.

### 2. Capa de Aplicación / Casos de Uso (`application`)
Orquesta las operaciones. Transforma las intenciones del usuario en interacciones con el Dominio.
- **Puertos de Entrada (Input Ports):** Son interfaces (ej. `CrearTarea`, `ListarTareas`) que declaran *qué* operaciones puede hacer nuestra aplicación. Los *Casos de Uso* implementan estas interfaces.
- **Puertos de Salida (Output Ports):** Son interfaces (ej. `TareaRepository`) que declaran *qué* necesita la aplicación del exterior (ej. guardar datos), sin importar *cómo* se hace.
- *Aislamiento:* Solo conoce el Dominio. No sabe si es llamado desde una API REST, la consola o un Swing GUI, ni sabe si los datos se guardan en MySQL o en Memoria.

### 3. Infraestructura / Adaptadores (`infrastructure`)
Es el puente entre nuestra aplicación y el mundo real. Convierte la tecnología específica en llamadas que la aplicación entiende y viceversa.
- **Adaptador de Entrada (UI - `TareaSwingUI`):** Es la interfaz gráfica con Java Swing. Actúa como el usuario que interactúa con la aplicación a través de los **Puertos de Entrada**. *No contiene lógica de negocio.*
- **Adaptador de Salida (Persistencia - `TareaRepositoryMemory`):** Es una implementación concreta del **Puerto de Salida**. En este caso, almacena los datos en un simple `HashMap` en memoria.

---

## 🛠️ Cómo Ejecutar el Proyecto

El proyecto está organizado usando las convenciones estándar de paquetes en Java. Puede ser ejecutado en cualquier IDE como IntelliJ IDEA, Eclipse o NetBeans.

1. Abre tu IDE de preferencia.
2. Abre el proyecto en el directorio raíz o importa los archivos ubicados en `src`.
3. Navega al paquete `main` y busca la clase `Main.java`.
4. Ejecuta el método `public static void main(String[] args)` ubicado en `Main.java`.

## 💡 Ventajas de esta implementación

- **Intercambiabilidad:** Si el día de mañana queremos guardar en una Base de Datos PostgreSQL en lugar de memoria, solo creamos `TareaRepositoryPostgres` y lo inyectamos en `Main`. Ningún Caso de Uso ni Entidad se entera del cambio.
- **Testing:** Podemos testear los `UseCase` inyectando un Repositorio Falso (Mock) sin necesidad de levantar una base de datos real o interfaces gráficas.
- **Limpieza:** La UI no ensucia la lógica con llamadas directas a base de datos ni validaciones complejas; se limita a pintar pantallas y reaccionar a clics.
