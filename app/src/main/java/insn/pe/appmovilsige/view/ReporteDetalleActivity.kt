package insn.pe.appmovilsige.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import insn.pe.appmovilsige.databinding.ActivityReporteDetalleBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.response.CargoEmpleadoResponse
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.view.ui.notifications.Personadataclass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReporteDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReporteDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReporteDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bundle = intent.extras
        val emergenciaId= bundle?.getString("solicitudemergenciaId")
        binding.tvdesemergencia.text= bundle?.getString("descEmergencia")
        binding.tvnsolicitud.text=emergenciaId.toString()
        obtenerDatosPaciente(emergenciaId)

    }
    private fun obtenerDatosPaciente(emergenciaId: String?)
    {
        val call: Call<List<Personadataclass>> =
            RetrofitCliente.retrofitService.buscarPacxSoliEmergenciaId(emergenciaId!!.toInt())
        call.enqueue( object : Callback<List<Personadataclass>> {
            override fun onResponse(call: Call<List<Personadataclass>>, response: Response<List<Personadataclass>>) {
                if (response.isSuccessful){
                    val respuesta=response.body()!![0]
                    binding.tvnombrepac.text=respuesta.nombre
                    binding.tvapellidopac.text=respuesta.apellido
                    binding.tvdnipac.text=respuesta.dni
                    obtenerDatosEmpleados(emergenciaId)
                    }
            }

            override fun onFailure(call: Call<List<Personadataclass>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun obtenerDatosEmpleados(emergenciaId: String?) {
        val call: Call<List<Personadataclass>> =
            RetrofitCliente.retrofitService.buscarEmpxSoliEmergenciaId(emergenciaId!!.toInt())
        call.enqueue( object : Callback<List<Personadataclass>> {
            override fun onResponse(call: Call<List<Personadataclass>>, response: Response<List<Personadataclass>>) {
                if (response.isSuccessful){
                    val emp1=response.body()!![0]
                    if (emp1!=null){
                        binding.tvnombrepa1.text=emp1.nombre+" "+emp1.apellido
                    }
                    val emp2=response.body()!![1]
                    if (emp2!=null){
                        binding.tvnombrepa2.text=emp2.nombre+" "+emp2.apellido
                    }
                    val emp3=response.body()!![2]
                    if (emp3!=null){
                        binding.tvnombrecho.text=emp3.nombre+" "+emp3.apellido
                    }
                    obtenerCargos(emergenciaId)
                }
            }

            override fun onFailure(call: Call<List<Personadataclass>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun obtenerCargos(emergenciaId: String) {
        val call: Call<List<CargoEmpleadoResponse>> =
            RetrofitCliente.retrofitService.buscarcargoxsoliId(emergenciaId!!.toInt())
        call.enqueue( object : Callback<List<CargoEmpleadoResponse>> {
            override fun onResponse(call: Call<List<CargoEmpleadoResponse>>, response: Response<List<CargoEmpleadoResponse>>) {
               if (response.isSuccessful){
                   try {
                       val cargo1 = response.body()!![0]
                       if (cargo1 != null) {
                           binding.tvcargopa1.text = cargo1.descCargo_PE
                       }
                       val cargo2 = response.body()!![1]
                       if (cargo2 != null) {
                           binding.tvcargopa2.text = cargo2.descCargo_PE
                       }
                       val cargo3 = response.body()!![2]
                       if (cargo3 != null) {
                           binding.tvcargocho.text = cargo3.descCargo_PE
                       }
                   }catch(t: Throwable){
                       t.printStackTrace()
                   }
                }
            }

            override fun onFailure(call: Call<List<CargoEmpleadoResponse>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}