package insn.pe.appmovilsige.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.common.MainViewModel
import insn.pe.appmovilsige.databinding.ActivitySolicitudEmergenciaGenerarBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.GenerarSolicitudEmergenciaRequest
import insn.pe.appmovilsige.retrofit.request.PacienteResponse
import insn.pe.appmovilsige.retrofit.request.PacienteSE
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*

class SolicitudEmergenciaGenerar : AppCompatActivity() {
    private lateinit var binding:ActivitySolicitudEmergenciaGenerarBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySolicitudEmergenciaGenerarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extra= intent.getStringExtra("coordenadas")
        val personaId=intent.getStringExtra("personaId")
        if(extra!=null) {
            binding.etubicacion.setText(extra)
            binding.etubicacion.isEnabled = false
        }
        //

        //
        binding.btncancelarsolicitud.setOnClickListener {
            startActivity(Intent(applicationContext,HomeActivity::class.java))
            finish()
        }
        binding.btnconfirmarsolicitud.setOnClickListener {
            if(binding.etdescripciN.text.toString()!=null&&binding.etdescripciN.text.toString()!="") {
                buscarPacientexPersonaId(personaId!!.toInt())
            }else{
                mostrarPantallaError()
            }
        }
        }


    private fun buscarPacientexPersonaId(personaId: Int) {
            val call: Call<List<PacienteResponse>> =
            RetrofitCliente.retrofitService.buscarpacientexpersonaId(personaId)
        call.enqueue( object : Callback<List<PacienteResponse>>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<PacienteResponse>>, response: Response<List<PacienteResponse>>) {
                if (response.isSuccessful){
                    val respuesta=response.body()!!
                    if (respuesta.size!=0) {
                        var paciente: PacienteResponse =respuesta.get(0)
                        var fechaActual=LocalDateTime.now()
                        val pacienteID=PacienteSE(paciente.pacienteId)

                        val solicitudEmergenciaRequest = GenerarSolicitudEmergenciaRequest(binding.etdescripciN.text.toString()
                            ,binding.etubicacion.text.toString()
                            ,fechaActual.toString()
                            ,pacienteID)
                        generarSoliEmergencia(solicitudEmergenciaRequest,personaId)

                    } else {
                        Toast.makeText(applicationContext,"algo ha salido mal",Toast.LENGTH_LONG).show()
                    }

                }else{
                    Toast.makeText(applicationContext,"la consulta no ha sido exitosa",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<PacienteResponse>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(applicationContext,""+t.printStackTrace(),Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun generarSoliEmergencia(solicitudEmergenciaRequest: GenerarSolicitudEmergenciaRequest,personaId: Int) {
        val call: Call<String> =
            RetrofitCliente.retrofitService.registrarSoliEmergencia(solicitudEmergenciaRequest)
        call.enqueue( object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    val respuesta=response.body()!!
                    if (respuesta!=null){
                        Toast.makeText(applicationContext,"Estamos buscando personal para tu emergencia.",Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext,EsperaActivity::class.java)
                        intent.putExtra("pacienteId",solicitudEmergenciaRequest.pacienteSE.pacienteId)
                        intent.putExtra("personaId",personaId)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(applicationContext,"Error de Solicitud",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"Error de Solicitud",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,"Estamos buscando personal para tu emergencia.",Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,EsperaActivity::class.java)
                intent.putExtra("pacienteId",solicitudEmergenciaRequest.pacienteSE.pacienteId)
                intent.putExtra("personaId",personaId)
                startActivity(intent)
                t.printStackTrace()
                finish()
            }

        })
    }

    private fun mostrarPantallaError(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de Registro. Verifique que los campos esten completos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

}
