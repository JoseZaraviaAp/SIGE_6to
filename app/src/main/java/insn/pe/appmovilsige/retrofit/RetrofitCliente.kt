package insn.pe.appmovilsige.retrofit


import insn.pe.appmovilsige.common.Constantes
import insn.pe.appmovilsige.retrofit.api.ApiRestService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCliente {

    private fun builderRetrofit() = Retrofit.Builder()
            .baseUrl(Constantes.URL_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val retrofitService: ApiRestService by lazy {
        builderRetrofit().create(ApiRestService::class.java)
    }
}