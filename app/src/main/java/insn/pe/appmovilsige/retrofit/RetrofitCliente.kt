package insn.pe.appmovilsige.retrofit


import insn.pe.appmovilsige.common.Constantes
import insn.pe.appmovilsige.retrofit.api.ApiAutenticacionService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCliente {

    private fun builderRetrofit() = Retrofit.Builder()
            .baseUrl(Constantes.URL_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val retrofitService: ApiAutenticacionService by lazy {
        builderRetrofit().create(ApiAutenticacionService::class.java)
    }
}