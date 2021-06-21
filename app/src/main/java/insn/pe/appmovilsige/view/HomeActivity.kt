package insn.pe.appmovilsige

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import insn.pe.appmovilsige.common.MainViewModel
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class ProviderType{
    NORMAL,
    GOOGLE,
    FACEBOOK
}


class HomeActivity : AppCompatActivity() {
        lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        val bundle=intent.extras
        var personaIdex=bundle?.getInt("personaId")
        if (personaIdex!=null) {
            val prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("personaId",personaIdex.toString())
            prefs.apply()
            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            mainViewModel.usuarioLogged.observe(this, {
                if(it.personaId!=0) {
                    personaIdex = it.personaId
                }
            })
            personaIdex?.let { actualizarDataProto(it) }
        }else{
            personaIdex=mainViewModel.usuarioLogged.value?.personaId
        }

    }

    private fun actualizarDataProto(personaIdex:Int) {
        val call: Call<List<LoginReponse>> =
            RetrofitCliente.retrofitService.buscarxId(personaIdex)
        call.enqueue( object : Callback<List<LoginReponse>> {
            override fun onResponse(call: Call<List<LoginReponse>>, response: Response<List<LoginReponse>>) {
                if (response.isSuccessful){
                    val respuesta=response.body()!!
                    if (respuesta.size!=0) {
                        val persona:LoginReponse =respuesta.get(0)
                        mainViewModel.actualizarValor(persona)
                    }

                }else{

                }
            }

            override fun onFailure(call: Call<List<LoginReponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}