package insn.pe.appmovilsige.view.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.R
import insn.pe.appmovilsige.databinding.FragmentGestpacientesBinding
import insn.pe.appmovilsige.databinding.FragmentReportesBinding
import insn.pe.appmovilsige.view.loginSIGE

class PacienteFragment : Fragment() {

    private lateinit var binding: FragmentGestpacientesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGestpacientesBinding.inflate(inflater, container, false)
        binding.btncerrarsesion.setOnClickListener {
            val prefs=this.activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            prefs?.clear()
            prefs?.apply()
            (activity as HomeActivity).mainViewModel.limpiarDatos()
            startActivity(Intent(context,loginSIGE::class.java))
            activity?.finish()
        }
        return binding.root
    }
}