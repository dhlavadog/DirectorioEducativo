
package co.edu.unal.directorioeducativo

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

// Representa cada departamento y sus nombres en la API
data class Departamento(
    val nombre: String,
    val valoresApi: List<String>
)

class MainActivity : AppCompatActivity() {

    private lateinit var spnDepartamento: Spinner
    private lateinit var btnBuscar: Button
    private lateinit var txtEstado: TextView
    private lateinit var layoutResultados: LinearLayout
    private lateinit var spnMunicipio: Spinner

    private var municipios: List<String> = emptyList()

    // Lista de departamentos y sus variantes en la API
    private val departamentos = listOf(

        Departamento("Amazonas", listOf("Amazonas", "AMAZONAS")),
        Departamento("Antioquia", listOf("Antioquia", "ANTIOQUIA")),
        Departamento("Arauca", listOf("Arauca", "ARAUCA")),

        Departamento(
            "Archipiélago de San Andrés, Providencia y Santa Catalina",
            listOf(
                "Archipiélago De San Andres,Providencia Y Santa Catalina",
                "ARCHIPIÉLAGO DE SAN ANDRES,PROVIDENCIA Y SANTA CATALINA",
                "Archipiélago de San Andrés, Providencia y Santa Catalina"
            )
        ),

        Departamento("Atlántico", listOf("Atlántico", "ATLÁNTICO")),

        Departamento(
            "Bogotá D.C.",
            listOf(
                "Bogotá, D.C.",
                "Capital Bogotá, D.C.",
                "CAPITAL BOGOTÁ, D.C."
            )
        ),

        Departamento("Bolívar", listOf("Bolívar", "BOLÍVAR")),
        Departamento("Boyacá", listOf("Boyacá", "BOYACÁ")),
        Departamento("Caldas", listOf("Caldas", "CALDAS")),
        Departamento("Caquetá", listOf("Caquetá", "CAQUETÁ")),
        Departamento("Casanare", listOf("Casanare", "CASANARE")),
        Departamento("Cauca", listOf("Cauca", "CAUCA")),
        Departamento("Cesar", listOf("Cesar", "CESAR")),
        Departamento("Chocó", listOf("Chocó", "CHOCÓ")),
        Departamento("Córdoba", listOf("Córdoba", "CÓRDOBA")),

        Departamento(
            "Cundinamarca",
            listOf("Cundinamarca", "CUNDINAMARCA", "Cunidnamarca")
        ),

        Departamento("Guainía", listOf("Guainía", "GUAINÍA")),

        Departamento(
            "La Guajira",
            listOf("Guajira", "GUAJIRA", "La Guajira")
        ),

        Departamento("Guaviare", listOf("Guaviare", "GUAVIARE")),
        Departamento("Huila", listOf("Huila", "HUILA")),
        Departamento("Magdalena", listOf("Magdalena", "MAGDALENA")),
        Departamento("Meta", listOf("Meta", "META")),
        Departamento("Nariño", listOf("Nariño", "NARIÑO")),

        Departamento(
            "Norte de Santander",
            listOf("Norte de Santander", "NORTE DE SANTANDER")
        ),

        Departamento("Putumayo", listOf("Putumayo", "PUTUMAYO")),
        Departamento("Quindío", listOf("Quindio", "QUINDIO")),
        Departamento("Risaralda", listOf("Risaralda", "RISARALDA")),
        Departamento("Santander", listOf("Santander", "SANTANDER")),
        Departamento("Sucre", listOf("Sucre", "SUCRE")),
        Departamento("Tolima", listOf("Tolima", "TOLIMA")),

        Departamento(
            "Valle del Cauca",
            listOf("Valle del Cauca", "VALLE DEL CAUCA")
        ),

        Departamento("Vaupés", listOf("Vaupés", "VAUPÉS")),
        Departamento("Vichada", listOf("Vichada", "VICHADA"))
    )

    // Consultar los municipios del departamento seleccionado
    private fun cargarMunicipios(position: Int) {

        val departamentoSeleccionado = departamentos[position]

        txtEstado.text = "Cargando municipios..."
        spnMunicipio.isEnabled = false

        ApiService.buscarMunicipios(
            departamentoSeleccionado.valoresApi
        ) { resultados, error ->

            spnMunicipio.isEnabled = true

            if (error != null) {

                actualizarSpinnerMunicipios(emptyList())
                txtEstado.text = "No se pudieron cargar los municipios.\n$error"

            } else if (resultados != null) {

                val listaMunicipios = mutableListOf<String>()

                for (i in 0 until resultados.length()) {

                    val municipio = resultados
                        .getJSONObject(i)
                        .optString("municipio")

                    if (municipio.isNotBlank()) {
                        listaMunicipios.add(municipio)
                    }
                }

                municipios = listaMunicipios.distinct().sorted()

                actualizarSpinnerMunicipios(municipios)

                txtEstado.text =
                    "Selecciona un municipio o consulta todo el departamento."
            }
        }
    }

    // Actualizar las opciones del selector
    private fun actualizarSpinnerMunicipios(
        listaMunicipios: List<String>
    ) {

        val opciones = listOf("Todos los municipios") + listaMunicipios

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opciones
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnMunicipio.adapter = adaptador
        spnMunicipio.setSelection(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Vincular los elementos de la interfaz
        spnDepartamento = findViewById(R.id.spnDepartamento)
        spnMunicipio = findViewById(R.id.spnMunicipio)
        btnBuscar = findViewById(R.id.btnBuscar)
        txtEstado = findViewById(R.id.txtEstado)
        layoutResultados = findViewById(R.id.layoutResultados)

        // 2. Crear el adaptador del Spinner
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            departamentos.map { it.nombre }
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnDepartamento.adapter = adaptador

        // Configurar inicialmente el selector de municipios
        actualizarSpinnerMunicipios(emptyList())

// Detectar cambios en el departamento seleccionado
        spnDepartamento.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    cargarMunicipios(position)
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }

        // 3. Configurar el botón de búsqueda
        btnBuscar.setOnClickListener {

            val departamentoSeleccionado =
                departamentos[spnDepartamento.selectedItemPosition]

            // Comprobar si se eligió un municipio concreto
            val municipioSeleccionado =
                if (spnMunicipio.selectedItemPosition == 0) {
                    null
                } else {
                    spnMunicipio.selectedItem.toString()
                }

            txtEstado.text = "Consultando establecimientos..."
            layoutResultados.removeAllViews()
            btnBuscar.isEnabled = false

            ApiService.buscarEstablecimientos(
                departamentoSeleccionado.valoresApi,
                municipioSeleccionado
            ) { establecimientos, error ->

                btnBuscar.isEnabled = true

                if (error != null) {

                    txtEstado.text = "Error al consultar la API:\n$error"

                } else if (establecimientos != null) {

                    mostrarResultados(
                        establecimientos,
                        departamentoSeleccionado.nombre,
                        municipioSeleccionado
                    )

                }
            }
        }
    }

    // 5. Mostrar los establecimientos en tarjetas
    private fun mostrarResultados(
        establecimientos: JSONArray,
        departamento: String,
        municipio: String?
    ) {

        layoutResultados.removeAllViews()

        if (establecimientos.length() == 0) {
            txtEstado.text = "No se encontraron establecimientos."
            return
        }

        val ubicacion = if (municipio.isNullOrBlank()) {
            departamento
        } else {
            "$municipio, $departamento"
        }

        txtEstado.text =
            "Consulta: $ubicacion\n" +
                    "Establecimientos encontrados: ${establecimientos.length()}"

        for (i in 0 until establecimientos.length()) {

            val establecimiento = establecimientos.getJSONObject(i)

            val nombre = establecimiento.optString(
                "nombre_establecimiento", "Sin nombre"
            )

            val municipio = establecimiento.optString(
                "municipio", "No disponible"
            )

            val departamento = establecimiento.optString(
                "departamento", "No disponible"
            )

            val sector = establecimiento.optString(
                "sector", "No disponible"
            )

            val calendario = establecimiento.optString(
                "calendario", "No disponible"
            )

            val matricula = establecimiento.optString(
                "total_matricula", "No disponible"
            )

            val sedes = establecimiento.optString(
                "cantidad_sedes", "No disponible"
            )

            val anio = establecimiento.optString(
                "a_o", "No disponible"
            )

            // Crear la tarjeta
            val tarjeta = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 20, 24, 20)

                background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius = 20f
                    setStroke(2, Color.LTGRAY)
                }

                elevation = 4f
            }

            val parametros = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }

            // Nombre del establecimiento
            agregarTexto(
                tarjeta,
                nombre,
                19f,
                true,
                Color.rgb(35, 75, 120)
            )

            // Ubicación
            agregarTexto(
                tarjeta,
                "$municipio · $departamento",
                14f,
                false,
                Color.DKGRAY
            )

            // Separador
            val separador = android.view.View(this).apply {
                setBackgroundColor(Color.LTGRAY)
            }

            tarjeta.addView(
                separador,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
            )

            // Información principal
            agregarTexto(
                tarjeta,
                "Matrícula: $matricula estudiantes",
                15f,
                true,
                Color.BLACK
            )

            agregarTexto(
                tarjeta,
                "Cantidad de sedes: $sedes",
                15f,
                false,
                Color.DKGRAY
            )

            agregarTexto(
                tarjeta,
                "Sector: $sector",
                14f,
                false,
                Color.DKGRAY
            )

            agregarTexto(
                tarjeta,
                "Calendario: $calendario",
                14f,
                false,
                Color.DKGRAY
            )

            agregarTexto(
                tarjeta,
                "Año de los datos: $anio",
                13f,
                false,
                Color.GRAY
            )

            layoutResultados.addView(tarjeta, parametros)
        }
    }

    // 6. Función auxiliar para agregar textos
    private fun agregarTexto(
        contenedor: LinearLayout,
        contenido: String,
        tamano: Float,
        negrita: Boolean,
        color: Int
    ) {

        val texto = TextView(this).apply {
            text = contenido
            textSize = tamano
            setTextColor(color)

            if (negrita) {
                setTypeface(null, Typeface.BOLD)
            }

            setPadding(0, 4, 0, 4)
        }

        contenedor.addView(texto)
    }
}
