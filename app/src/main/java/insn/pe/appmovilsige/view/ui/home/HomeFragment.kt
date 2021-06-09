package insn.pe.appmovilsige.view.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.databinding.FragmentHomeBinding
import insn.pe.appmovilsige.view.UbicacionActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.ivbtnemergencia.setOnClickListener{
            val personaId= (activity as HomeActivity).mainViewModel.usuarioLogged.value!!.personaId
            val intent= Intent(this.context, UbicacionActivity::class.java)
            intent.putExtra("personaId",personaId)
            startActivity(intent)
        }

        return binding.root
    }
}