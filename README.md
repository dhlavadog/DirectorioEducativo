# Directorio Educativo de Colombia 🇨🇴

Aplicación Android desarrollada en Kotlin que permite consultar establecimientos educativos de Colombia mediante el consumo de un servicio web de Datos Abiertos del Gobierno colombiano.

El proyecto fue desarrollado como parte de un reto académico cuyo objetivo principal es aprender a consumir servicios web desde una aplicación Android y procesar la información recibida.

## 📱 Características

- **Consulta por departamento:** permite seleccionar cualquiera de los departamentos de Colombia.
- **Filtro por municipio:** permite consultar los establecimientos de un municipio específico o mantener la búsqueda general del departamento.
- **Consulta dinámica:** los municipios disponibles se obtienen directamente desde el servicio web según el departamento seleccionado.
- **Información de establecimientos:** presenta los resultados en tarjetas individuales.
- **Manejo de errores:** identifica problemas de conexión, tiempos de espera agotados y errores de comunicación con el servidor.
- **Resultados organizados:** muestra hasta 50 establecimientos por consulta.

## 📊 Información consultada

La aplicación obtiene información del conjunto de datos:

**MEN - Establecimientos Educativos Preescolar, Básica y Media**

Fuente: [Datos Abiertos Colombia](https://www.datos.gov.co/)

Identificador del conjunto de datos: `cfw5-qzt5`

Entre los campos consultados se encuentran:

| Campo | Descripción |
|---|---|
| Nombre del establecimiento | Nombre de la institución educativa |
| Departamento | Departamento donde se encuentra |
| Municipio | Municipio donde se encuentra |
| Sector | Sector educativo |
| Calendario | Calendario académico |
| Matrícula total | Número de estudiantes matriculados |
| Cantidad de sedes | Número de sedes del establecimiento |
| Año | Año de los datos |

## 🛠️ Tecnologías utilizadas

- **Kotlin:** lenguaje de programación.
- **Android Studio:** entorno de desarrollo.
- **XML:** diseño de la interfaz gráfica.
- **HTTP GET:** comunicación con el servicio web.
- **JSON:** formato de recepción y procesamiento de datos.
- **HttpURLConnection:** gestión de las conexiones HTTP.
- **Gradle:** configuración y construcción del proyecto.
- **Datos Abiertos Colombia (Socrata):** proveedor de información.

## ⚙️ Funcionamiento

La aplicación sigue este proceso:

1. El usuario selecciona un departamento.
2. La aplicación consulta el servicio web para obtener los municipios correspondientes.
3. El usuario puede seleccionar un municipio o conservar la opción «Todos los municipios».
4. Se construye una consulta utilizando los filtros seleccionados.
5. La aplicación envía una petición HTTP GET al servicio web.
6. La respuesta JSON se procesa mediante Kotlin.
7. Los establecimientos encontrados se presentan en la interfaz.

## 🔐 Configuración del App Token

Para consumir el servicio web se necesita un App Token de Datos Abiertos Colombia.

Por razones de seguridad, el token no está incluido directamente en el código fuente. Se utiliza un archivo local de configuración llamado `local.properties`.

Agrega la siguiente propiedad a ese archivo:

```properties
DATOS_APP_TOKEN=TU_APP_TOKEN
```

El archivo `local.properties` debe permanecer excluido del repositorio mediante `.gitignore`.

Gradle lee esta propiedad y la proporciona a la aplicación mediante `BuildConfig`.

**Importante:** el token se incorpora al APK compilado, por lo que no debe considerarse un secreto completamente protegido.

## 📋 Requisitos

- Android Studio.
- JDK compatible con la configuración de Gradle del proyecto.
- Android SDK API 37.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Conexión a Internet.
- App Token de Datos Abiertos Colombia.

## 🚀 Instalación

1. Clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

2. Abrir el proyecto en Android Studio.

3. Crear o configurar el archivo `local.properties` con el App Token.

4. Sincronizar el proyecto con Gradle.

5. Ejecutar la aplicación desde Android Studio.

## 🎯 Objetivo académico

Este proyecto busca poner en práctica los conceptos fundamentales del consumo de servicios web en Android:

- Realización de peticiones HTTP.
- Autenticación mediante cabeceras.
- Recepción y procesamiento de respuestas JSON.
- Ejecución de operaciones de red en segundo plano.
- Comunicación entre hilos.
- Presentación dinámica de información obtenida desde una API.
- Gestión de errores durante la comunicación con servicios externos.

El filtro por municipio y las mejoras de presentación se incorporaron como funcionalidades adicionales al objetivo original del reto.

---

**Proyecto académico — Directorio Educativo de Colombia.**