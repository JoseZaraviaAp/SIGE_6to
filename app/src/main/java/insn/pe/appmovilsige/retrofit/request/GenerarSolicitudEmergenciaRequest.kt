package insn.pe.appmovilsige.retrofit.request

data class GenerarSolicitudEmergenciaRequest(
    val descripcion: String,
    val ubicacion: String,
    val fecha: String,
    val pacienteSE: PacienteSE

)