package insn.pe.appmovilsige.retrofit.request

data class PacienteResponse(
    val alergias_USU: String,
    val numero_contacto_USU: String,
    val pacienteId: Int,
    val personaId: Int,
    val peso_PA: Double,
    val talla_PA: Double,
    val tipo_sangre_USU: String
)