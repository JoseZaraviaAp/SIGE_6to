package insn.pe.appmovilsige.retrofit.api

import insn.pe.appmovilsige.UsuarioRequest
import insn.pe.appmovilsige.retrofit.request.*
import insn.pe.appmovilsige.retrofit.response.CargoEmpleadoResponse
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.retrofit.response.RegistroPaResponse
import insn.pe.appmovilsige.view.ui.notifications.Personadataclass
import insn.pe.appmovilsige.view.ui.notifications.reporte
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface   ApiRestService {
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
    fun registrarEmergencia(@Body pacienteSE: PacienteSE):Call<List<LoginReponse>>

    @POST("paciente/v1/listar")
    fun editarPaciente(@Body pacienteSE: PacienteSE):Call<String>

    @GET("solicitudemergencia/v1/listar/{pacienteId}")
    fun listarxpacienteId(@Path ("pacienteId") pacienteId:Int): Call<List<reporte>>

    @GET("persona/v1/buscarPacxSoliEmergenciaId/{solicitudEmergenciaId}")
    fun buscarPacxSoliEmergenciaId(@Path ("solicitudEmergenciaId") solicitudEmergenciaId:Int): Call<List<Personadataclass>>

    @GET("persona/v1/buscarEmpxSoliEmergenciaId/{solicitudEmergenciaId}")
    fun buscarEmpxSoliEmergenciaId(@Path ("solicitudEmergenciaId") solicitudEmergenciaId:Int): Call<List<Personadataclass>>

    @GET("cargoempleado/v1/buscarcargoxsoliId/{solicitudEmergenciaId}")
    fun buscarcargoxsoliId(@Path ("solicitudEmergenciaId") solicitudEmergenciaId:Int): Call<List<CargoEmpleadoResponse>>
}