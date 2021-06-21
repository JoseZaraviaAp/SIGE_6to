package insn.pe.appmovilsige.view.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import insn.pe.appmovilsige.R
import insn.pe.appmovilsige.databinding.FragmentHomeBinding
import insn.pe.appmovilsige.databinding.FragmentReportesBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.GenerarSolicitudEmergenciaRequest
import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.request.PacienteResponse
import insn.pe.appmovilsige.retrofit.request.PacienteSE
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.view.ReporteDetalleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ReporteFragment : Fragment() {

    lateinit var binding: FragmentReportesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportesBinding.inflate(inflater, container, false)
        val prefs= this.activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var personaId= prefs?.getString("personaId",null)?.toInt()
        if (personaId != null) {

            buscarPacientexPersonaId(personaId)
        }

        return binding.root
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
                        obtenerTreportes(paciente.pacienteId)
                    }
                }
            }

            override fun onFailure(call: Call<List<PacienteResponse>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun obtenerTreportes(pacienteId: Int) {
        val call: Call<List<reporte>> =
            RetrofitCliente.retrofitService.listarxpacienteId(pacienteId)
        call.enqueue( object : Callback<List<reporte>> {
            override fun onResponse(call: Call<List<reporte>>, response: Response<List<reporte>>) {
                if (response.isSuccessful){
                    val listaReportes=response.body()!!
                    var listaTitulos:ArrayList<String> = ArrayList()
                    listaReportes.forEach {
                        listaTitulos.add(it.descripcion+" | "+it.fecha)
                    }
                    val arrayAdapter:ArrayAdapter<String>
                    arrayAdapter= ArrayAdapter(activity!!.applicationContext,
                        android.R.layout.simple_list_item_1,
                        listaTitulos)
                    binding.lvreporte.adapter=arrayAdapter
                    binding.lvreporte.setOnItemClickListener { parent, view, position, id ->
                        val intent = Intent(context,ReporteDetalleActivity::class.java)
                        intent.putExtra("solicitudemergenciaId",listaReportes[position].solicitudemergenciaId)
                        intent.putExtra("descEmergencia",listaReportes[position].descripcion)
                        startActivity(intent)
                    }
                }else{
                }
            }
            override fun onFailure(call: Call<List<reporte>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}