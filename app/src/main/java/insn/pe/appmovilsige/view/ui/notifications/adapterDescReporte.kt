package insn.pe.appmovilsige.view.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import insn.pe.appmovilsige.databinding.ItemListareporteBinding

class adapterDescReporte(val descEmergencia:String
, val soliemergenciaId: Int,private val listPersona: List<Personadataclass>)
    :RecyclerView.Adapter<adapterDescReporte.ViewHolder>()
{
    inner class ViewHolder (val binding: ItemListareporteBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemListareporteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listPersona[position]){
                binding.tvnsolicitud.text=soliemergenciaId.toString()
                binding.tvnombrepac.text=nombre
                binding.tvapellidopac.text=apellido
                binding.tvdnipac.text=dni
                binding.tvdesemergencia.text=descEmergencia
            }
        }
    }

    override fun getItemCount()=listPersona.size

}