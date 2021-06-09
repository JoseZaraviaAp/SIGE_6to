package insn.pe.appmovilsige.retrofit.api

import insn.pe.appmovilsige.UsuarioRequest
import insn.pe.appmovilsige.retrofit.request.*
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.retrofit.response.RegistroPaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiAutenticacionService {
    @POST("persona/v1/validarEmail")
    fun autenticacion(@Body loginRequest: LoginRequest): Call<List<LoginReponse>>

    @POST("paciente/v1/registrarPaciente")
    fun registroPaciente(@Body RegistroRequest: RegistroRequest): Call<List<RegistroPaResponse>>

    @GET("persona/v1/buscarEmail/{email}")
    fun buscarxEmail(@Path("email")email:String):Call<List<LoginReponse>>

    @POST("solicitudemergencia/v1/registrar")
    fun registrarSoliEmergencia(@Body generarSolicitudEmergenciaRequest: GenerarSolicitudEmergenciaRequest) : Call<String>

    @GET("persona/v1/listar/{personaId}")
    fun buscarxId(@Path("personaId") personaId: Int):Call<List<LoginReponse>>
    //
    @GET("paciente/v1/listarxpersonaId/{personaId}")
    fun buscarpacientexpersonaId(@Path("personaId")personaId:Int):Call<List<PacienteResponse>>

    @POST("empleado_solicitudemergencia/v1/registrarEmpSoliEm")
    fun registrarEmergencia(@Body pacienteSE: PacienteSE)
}