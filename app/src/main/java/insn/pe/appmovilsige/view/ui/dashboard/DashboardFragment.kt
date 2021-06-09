package insn.pe.appmovilsige.view.ui.dashboard

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
import insn.pe.appmovilsige.databinding.FragmentReportesBinding
import insn.pe.appmovilsige.view.loginSIGE

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentReportesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportesBinding.inflate(inflater, container, false)
        binding.btncerrarsesion.setOnClickListener {
            (activity as HomeActivity).mainViewModel.limpiarDatos()
            startActivity(Intent(context,loginSIGE::class.java))
            activity?.onBackPressed()
            activity?.finish();
        }
        return binding.root
    }



}