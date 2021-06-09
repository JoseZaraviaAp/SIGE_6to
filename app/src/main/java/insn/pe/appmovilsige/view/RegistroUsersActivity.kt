package insn.pe.appmovilsige.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import insn.pe.appmovilsige.HomeActivity
import insn.pe.appmovilsige.databinding.ActivityRegistroUsersBinding
import insn.pe.appmovilsige.retrofit.RetrofitCliente
import insn.pe.appmovilsige.retrofit.request.LoginRequest
import insn.pe.appmovilsige.retrofit.request.RegistroRequest
import insn.pe.appmovilsige.retrofit.response.LoginReponse
import insn.pe.appmovilsige.retrofit.response.RegistroPaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RegistroUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistroUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailExtra=intent.getStringExtra("email")
        if (emailExtra!=null){
            binding.etemail.setText(emailExtra)
        }

        binding.btnregresar.setOnClickListener {
            startActivity(Intent(applicationContext,loginSIGE::class.java))
        }
        binding.btnregistraruser.setOnClickListener {
            val RegistroRequest = RegistroRequest(binding.etdni.text.toString(),
                binding.etnombre.text.toString(),
                binding.etapellido.text.toString(),
                binding.etfechanac.text.toString(),
                binding.etdireccion.text.toString(),
                "no image","1",
                binding.ettiposangre.text.toString(),
                binding.etpeso.text.toString(),
                binding.ettalla.text.toString(),
                binding.etnumero.text.toString(),
                binding.etalergias.text.toString(),
                binding.etemail.text.toString(),
                binding.etemail.text.toString(),
                binding.etpassword.text.toString())

            val call: Call<List<RegistroPaResponse>> = RetrofitCliente.retrofitService.registroPaciente(RegistroRequest)
            call.enqueue( object : Callback<List<RegistroPaResponse>> {
                override fun onResponse(call: Call<List<RegistroPaResponse>>, response: Response<List<RegistroPaResponse>>) {
                    if (response.isSuccessful){
                        val respuesta=response.body()!!
                        if (respuesta.size!=0){
                            irActivityHome(RegistroRequest.email,"RETrofit")
                        }else{
                            mostrarPantallaError()
                        }
                    }else{
                        mostrarPantallaError()
                    }
                }
                override fun onFailure(call: Call<List<RegistroPaResponse>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    }
    private fun irActivityHome(email :String, provider: String){
        var intent = Intent(this, loginSIGE::class.java)
        intent.putExtra("email",email)
        intent.putExtra("provider",provider)
        startActivity(intent)

    }
    private fun mostrarPantallaError(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de Registro")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
}