package insn.pe.appmovilsige.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.R
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.PacienteSE
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EsperaActivity : AppCompatActivity() {
    private val TIEMPO_REINTENTO:Long = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_espera)

        val bundle=intent.extras
        var pacienteextra=bundle?.getInt("pacienteId")
        var personaId=bundle?.getInt("personaId")
            if (pacienteextra!=null) {
                val pacienteId = PacienteSE(pacienteextra)
                       registrarEmergencia(pacienteId, personaId)
           }

    }

    private fun registrarEmergencia(pacienteId: PacienteSE, personaId:Int?) {
        try {
            val call: Call<List<LoginReponse>> =
                RetrofitCliente.retrofitService.registrarEmergencia(pacienteId)
            call.enqueue(object : Callback<List<LoginReponse>> {
                override fun onResponse(
                    call: Call<List<LoginReponse>>,
                    response: Response<List<LoginReponse>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()!!
                        if (respuesta.size != 0) {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.putExtra("personaId", personaId)
                            startActivity(intent)

                        }
                    }else {
                        Handler().postDelayed({
                            registrarEmergencia(pacienteId, personaId)
                        }, TIEMPO_REINTENTO)
                    }
                }

                override fun onFailure(call: Call<List<LoginReponse>>, t: Throwable) {
                }
            })
        }catch (e: Exception){
            Handler().postDelayed({
                registrarEmergencia(pacienteId, personaId)
            }, TIEMPO_REINTENTO)
        }
    }
}