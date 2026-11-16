
package co.edu.unal.directorioeducativo

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException
import java.util.concurrent.Executors

object ApiService {

    private const val APP_TOKEN = BuildConfig.DATOS_APP_TOKEN

    private const val URL_API =
        "https://www.datos.gov.co/api/v3/views/cfw5-qzt5/query.json"

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    // 1. Obtener los municipios de un departamento
    fun buscarMunicipios(
        departamentos: List<String>,
        callback: (JSONArray?, String?) -> Unit
    ) {

        val valoresDepartamento = departamentos.joinToString(", ") {
            "'${it.replace("'", "''")}'"
        }

        val consulta = """
            SELECT municipio
            WHERE departamento IN ($valoresDepartamento)
            AND municipio IS NOT NULL
            AND municipio != ''
            GROUP BY municipio
            ORDER BY municipio
        """.trimIndent()

        ejecutarConsulta(consulta, callback)
    }

    // 2. Buscar establecimientos de un departamento
    //    y, opcionalmente, de un municipio concreto
    fun buscarEstablecimientos(
        departamentos: List<String>,
        municipio: String? = null,
        callback: (JSONArray?, String?) -> Unit
    ) {

        val valoresDepartamento = departamentos.joinToString(", ") {
            "'${it.replace("'", "''")}'"
        }

        val filtroMunicipio = if (
            municipio.isNullOrBlank()
        ) {
            ""
        } else {
            "AND municipio = '${municipio.replace("'", "''")}'"
        }

        val consulta = """
            SELECT a_o, departamento, municipio,
            nombre_establecimiento, sector, calendario,
            total_matricula, cantidad_sedes
            WHERE departamento IN ($valoresDepartamento)
            $filtroMunicipio
            LIMIT 50
        """.trimIndent()

        ejecutarConsulta(consulta, callback)
    }

    // 3. Ejecutar las consultas y gestionar la conexión HTTP
    private fun ejecutarConsulta(
        consulta: String,
        callback: (JSONArray?, String?) -> Unit
    ) {

        executor.execute {

            var connection: HttpURLConnection? = null

            try {

                val consultaCodificada =
                    URLEncoder.encode(consulta, "UTF-8")

                val url = URL("$URL_API?query=$consultaCodificada")

                connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connectTimeout = 15000
                connection.readTimeout = 20000

                connection.setRequestProperty(
                    "X-App-Token", APP_TOKEN
                )

                connection.setRequestProperty(
                    "Accept", "application/json"
                )

                val codigoRespuesta = connection.responseCode

                if (codigoRespuesta == HttpURLConnection.HTTP_OK) {

                    val respuesta = connection.inputStream
                        .bufferedReader()
                        .use { it.readText() }

                    val resultados = JSONArray(respuesta)

                    mainHandler.post {
                        callback(resultados, null)
                    }

                } else {

                    val error = connection.errorStream
                        ?.bufferedReader()
                        ?.use { it.readText() }

                    throw Exception(
                        "Error HTTP $codigoRespuesta: $error"
                    )
                }

            } catch (e: SocketTimeoutException) {

                mainHandler.post {
                    callback(
                        null,
                        "El servidor tardó demasiado en responder. Inténtalo nuevamente."
                    )
                }

            } catch (e: UnknownHostException) {

                mainHandler.post {
                    callback(
                        null,
                        "No se pudo conectar con el servidor. Comprueba tu conexión a Internet."
                    )
                }

            } catch (e: IOException) {

                mainHandler.post {
                    callback(
                        null,
                        "Ocurrió un problema de comunicación con el servidor."
                    )
                }

            } catch (e: Exception) {

                mainHandler.post {
                    callback(
                        null,
                        e.message ?: "Ocurrió un error inesperado."
                    )
                }

            } finally {

                connection?.disconnect()
            }
        }
    }
}
