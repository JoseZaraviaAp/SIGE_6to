package insn.pe.appmovilsige.retrofit.api

import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.request.RegistroRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.retrofit.response.RegistroPaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAutenticacionService {
    @POST("persona/v1/validarEmail")
    fun autenticacion(@Body loginRequest: LoginRequest): Call<List<LoginReponse>>

    @POST("paciente/v1/registrarPaciente")
    fun registroPaciente(@Body RegistroRequest: RegistroRequest): Call<List<RegistroPaResponse>>
}