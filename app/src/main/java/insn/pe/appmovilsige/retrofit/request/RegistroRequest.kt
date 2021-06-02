package insn.pe.appmovilsige.retrofit.request

import java.util.Date;

data class RegistroRequest(
    var dni: String,
    var nombre: String,
    var apellido: String,
    var fechanac: String,
    var direccion: String,
    var thumbnail: String,
    var estado: String,
    var tipoSangre: String,
    var peso: String,
    var talla: String,
    var numeroContacto: String,
    var alergias: String,
    var username: String,
    var email: String,
    var passwordu: String
)